package com.capstone.csdrms.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet; // Correct Sheet import
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.capstone.csdrms.Entity.SuspensionEntity;
import com.capstone.csdrms.Entity.StudentEntity;
import com.capstone.csdrms.Entity.RecordEntity;
import com.capstone.csdrms.Repository.SuspensionRepository;

import jakarta.transaction.Transactional;

import com.capstone.csdrms.Repository.RecordRepository;
import com.capstone.csdrms.Repository.StudentRepository;
import com.capstone.csdrms.Repository.NotificationRepository;
import com.capstone.csdrms.Repository.UserNotificationRepository;
import com.capstone.csdrms.Entity.NotificationEntity;
import java.util.stream.Collectors;

@Service
public class StudentService {

	@Autowired
	StudentRepository studentRepository;

	@Autowired
	RecordRepository recordRepository;

	@Autowired
	SuspensionRepository suspensionRepository;

	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	UserNotificationRepository userNotificationRepository;

	@Autowired
	ActivityLogService activityLogService;

	public boolean studentExists(String sid, String schoolYear) {
		return studentRepository.existsBySidAndSchoolYear(sid, schoolYear);
	}

	public StudentEntity insertStudent(StudentEntity student, Long initiator) {
		if (studentExists(student.getSid(), student.getSchoolYear())) {
			throw new IllegalStateException("Student with this ID and school year already exists.");
		}

		List<StudentEntity> existingStudents = studentRepository.findAllBySid(student.getSid());

		if (!existingStudents.isEmpty()) {
			for (StudentEntity existingStudent : existingStudents) {
				existingStudent.setCurrent(0);
				studentRepository.save(existingStudent);
			}
		}
		activityLogService.logActivity("Student Added",
				"Student " + student.getSid() + " (" + student.getName() + ")" + " added by SSO", initiator);
		return studentRepository.save(student);
	}

	public List<StudentEntity> getCurrentStudents() {
		return studentRepository.findAllByCurrent(1);
	}

	public List<StudentEntity> getStudentsByAdviser(int grade, String section, String schoolYear) {
		return studentRepository.findByCurrentAndGradeAndSectionAndSchoolYear(1, grade, section, schoolYear);
	}

	public StudentEntity updateStudent(Long id, StudentEntity studentDetails, Long initiator) {
		// Find the existing student by ID
		StudentEntity existingStudent = studentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Student not found for id: " + id));

		// Update fields
		existingStudent.setSid(studentDetails.getSid());
		existingStudent.setName(studentDetails.getName());
		existingStudent.setGrade(studentDetails.getGrade());
		existingStudent.setSection(studentDetails.getSection());
		existingStudent.setGender(studentDetails.getGender());
		existingStudent.setEmail(studentDetails.getEmail());
		existingStudent.setHomeAddress(studentDetails.getHomeAddress());
		// existingStudent.setContactNumber(studentDetails.getContactNumber());
		existingStudent.setEmergencyNumber(studentDetails.getEmergencyNumber());
		existingStudent.setSchoolYear(studentDetails.getSchoolYear());
		existingStudent.setCurrent(studentDetails.getCurrent());

		activityLogService.logActivity("Student Edited", "Student " + existingStudent.getSid() + " ("
				+ existingStudent.getName() + ")" + " information edited by SSO", initiator);
		// Save the updated student back to the database
		return studentRepository.save(existingStudent);
	}

	@Transactional
	public void deleteLatestAndSetPreviousAsCurrent(Long id, Long initiator) {
		Optional<StudentEntity> optionalStudent = studentRepository.findById(id);
		if (optionalStudent.isPresent()) {
			StudentEntity student = optionalStudent.get();
			String sid = student.getSid();

			List<RecordEntity> studentRecords = recordRepository.findAllById(id);
			if (!studentRecords.isEmpty()) {
				List<Long> recordIds = studentRecords.stream().map(RecordEntity::getRecordId)
						.collect(Collectors.toList());

				// Delete UserNotifications related to these records
				List<NotificationEntity> notifications = studentRecords.stream()
						.flatMap(r -> notificationRepository.findByRecordId(r.getRecordId()).stream())
						.collect(Collectors.toList());

				if (!notifications.isEmpty()) {
					List<Long> notificationIds = notifications.stream().map(NotificationEntity::getNotificationId)
							.collect(Collectors.toList());
					userNotificationRepository.deleteAllByNotification_NotificationIdIn(notificationIds);
					notificationRepository.deleteAllByRecordIdIn(recordIds);
				}

				suspensionRepository.deleteAllByRecord_Id(id);
				recordRepository.deleteAllById(id);
			}

			studentRepository.delete(student);

			activityLogService.logActivity("Student Deleted", "Student " + student.getSid() + " (" + student.getName()
					+ ")" + " and its associated records, reports, and suspension deleted by SSO", initiator);

			// Find the previous record by sorting by `schoolYear` in descending order
			List<StudentEntity> sortedStudents = studentRepository.findStudentsBySidOrderBySchoolYearDesc(sid);
			if (!sortedStudents.isEmpty()) {
				// Set the first record in the sorted list to `current = 1`
				StudentEntity previousStudent = sortedStudents.get(0);
				previousStudent.setCurrent(1);
				studentRepository.save(previousStudent);
			}

		} else {
			throw new RuntimeException("Student not found for id: " + id);
		}

	}

	// @SuppressWarnings("finally")
	// public StudentEntity updateStudent(String sid, StudentEntity
	// newStudentDetails) {
	// StudentEntity student = new StudentEntity();
	// try {
	// student = srepo.findBySid(sid);
	//
	// student.setFirstname(newStudentDetails.getFirstname());
	// student.setMiddlename(newStudentDetails.getMiddlename());
	// student.setLastname(newStudentDetails.getLastname());
	// student.setGrade(newStudentDetails.getGrade());
	// student.setSection(newStudentDetails.getSection());
	// student.setCon_num(newStudentDetails.getCon_num());
	//
	//
	// } catch (NoSuchElementException ex) {
	// throw ex;
	// } finally {
	// return srepo.save(student);
	// }
	// }
	//

	// public String deleteStudent(String sid) {
	// Optional<StudentEntity> existingStudent = srepo.findBySid(sid);
	//
	// List<FeedbackEntity> existingFeedbacksByStudent =
	// feedbackrepo.findALLByCaseEntity_Sid(sid);
	// List<StudentReportEntity> existingReportsByStudent =
	// studentrepo.findAllBySid(sid);
	// List<CaseEntity> existingCasesByStudent = caserepo.findAllBySid(sid);
	//
	// List<FollowupEntity> existingFollowupsByStudent =
	// followuprepo.findAllByCaseEntity_Sid(sid);
	// List<SanctionEntity> existingSanctionsByStudent =
	// sanctionrepo.findBySid(sid);
	//
	// studentrepo.deleteAll(existingReportsByStudent);
	// sanctionrepo.deleteAll(existingSanctionsByStudent);
	// feedbackrepo.deleteAll(existingFeedbacksByStudent);
	// followuprepo.deleteAll(existingFollowupsByStudent);
	// caserepo.deleteAll(existingCasesByStudent);
	// srepo.delete(existingStudent);
	//
	// return "Student " + sid + " is successfully deleted!";
	//// if (existingStudent != null) {
	//// srepo.delete(existingStudent);
	//// return "Student " + sid + " is successfully deleted!";
	//// } else {
	//// return "Student " + sid + " does not exist";
	//// }
	// }

	public static int getGrade(int number) {
		switch (number) {
			case 4:
				return 10;
			case 3:
				return 9;
			case 2:
				return 8;
			case 1:
				return 7;
			default:
				throw new IllegalArgumentException("Invalid number: " + number);
		}
	}

	public String formatSection(String section) {
		// Use regex to remove any numeric suffix after a dash
		return section.replaceAll("-\\d+$", "");
	}

	public void importStudentData(MultipartFile file, String schoolYear) throws Exception {
		List<StudentEntity> students = new ArrayList<>();

		// Pre-check for existing students before processing
		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue; // Skip header row

				String sid = null;
				Cell sidCell = row.getCell(0);
				if (sidCell != null) {
					if (sidCell.getCellType() == CellType.NUMERIC) {
						sid = String.valueOf((long) sidCell.getNumericCellValue());
					} else if (sidCell.getCellType() == CellType.STRING) {
						sid = sidCell.getStringCellValue();
					}
				}

				// Check if the student with this SID and school year already exists
				if (sid != null && studentExists(sid, schoolYear)) {
					throw new Exception("Import aborted: Student with SID " + sid + " and school year " + schoolYear
							+ " already exists.");
				}
			}
		}

		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
			Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

			for (Row row : sheet) {
				if (row.getRowNum() == 0)
					continue; // Skip header row

				StudentEntity student = new StudentEntity();

				String sid = null;
				Cell sidCell = row.getCell(0);
				if (sidCell != null) {
					if (sidCell.getCellType() == CellType.NUMERIC) {
						sid = String.valueOf((long) sidCell.getNumericCellValue());
					} else if (sidCell.getCellType() == CellType.STRING) {
						sid = sidCell.getStringCellValue();
					}
				}
				student.setSid(sid);

				String name = "";
				Cell firstNameCell = row.getCell(1);
				Cell lastNameCell = row.getCell(2);
				if (firstNameCell != null && firstNameCell.getCellType() == CellType.STRING) {
					name = firstNameCell.getStringCellValue().trim().replaceAll("\\s+", " ");
				}
				if (lastNameCell != null && lastNameCell.getCellType() == CellType.STRING) {
					name += ", " + lastNameCell.getStringCellValue().trim().replaceAll("\\s+", " ");
				}
				if (row.getCell(3) != null && row.getCell(3).getStringCellValue().trim().length() > 0) {
					name += " " + row.getCell(3).getStringCellValue().trim().replaceAll("\\s+", " ");
				}
				student.setName(name);

				int gradeNumber = 0;
				Cell gradeCell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK); // Ensure blank cells are
																								// treated as null
				if (gradeCell != null) {
					switch (gradeCell.getCellType()) {
						case NUMERIC:
							// If the cell is of numeric type, cast to int directly
							gradeNumber = (int) gradeCell.getNumericCellValue();
							break;
						case STRING:
							// If the cell is a string, try parsing it as an integer
							try {
								gradeNumber = Integer.parseInt(gradeCell.getStringCellValue().trim());
							} catch (NumberFormatException e) {
								// Handle the case where the string cannot be parsed as an integer
								throw new IllegalArgumentException("Grade column contains non-integer string value: "
										+ gradeCell.getStringCellValue().trim());
							}
							break;
						case BLANK:
							// Handle blank cells by assigning a default value or skipping
							gradeNumber = -1; // Example: Use -1 as a sentinel value for unknown or missing grades
							break;
						default:
							// Handle any unexpected cell type here
							throw new IllegalArgumentException(
									"Unexpected cell type in grade column: " + gradeCell.getCellType());
					}
				}

				// Optionally skip processing this row if the grade number is set to the
				// sentinel value
				if (gradeNumber == -1) {
					continue; // Skip to the next iteration of the loop if grade number is invalid
				}

				// Now you can use gradeNumber as an int
				int grade = getGrade(gradeNumber);
				student.setGrade(grade);

				Cell sectionCell = row.getCell(6);
				if (sectionCell != null && sectionCell.getCellType() == CellType.STRING) {
					student.setSection(formatSection(sectionCell.getStringCellValue()));
				}

				Cell genderCell = row.getCell(7);
				if (genderCell != null && genderCell.getCellType() == CellType.STRING) {
					student.setGender(genderCell.getStringCellValue());
				}

				Cell addressCell = row.getCell(8);
				if (addressCell != null && addressCell.getCellType() == CellType.STRING) {
					student.setHomeAddress(addressCell.getStringCellValue());
				}

				Cell emailCell = row.getCell(9);
				if (emailCell != null && emailCell.getCellType() == CellType.STRING) {
					student.setEmail(emailCell.getStringCellValue());
				}

				Cell emergencyCell = row.getCell(10);
				if (emergencyCell != null && emergencyCell.getCellType() == CellType.STRING) {
					student.setEmergencyNumber(emergencyCell.getStringCellValue());
				}

				// Handle School Year
				student.setSchoolYear(schoolYear);

				student.setCurrent(1);

				Optional<StudentEntity> existingStudent = studentRepository.findBySidAndCurrent(sid, 1);
				if (existingStudent.isPresent() && !existingStudent.get().getSchoolYear().equals(schoolYear)) {
					// Set current status of the existing student to 0
					StudentEntity oldStudent = existingStudent.get();
					oldStudent.setCurrent(0);
					studentRepository.save(oldStudent);
				}

				students.add(student);
			}
		}

		studentRepository.saveAll(students);
	}

}