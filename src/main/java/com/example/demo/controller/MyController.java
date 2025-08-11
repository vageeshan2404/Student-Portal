package com.example.demo.controller;

import com.example.demo.dto.AssignmentStatusDTO;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.SubmissionRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

import java.security.Principal;


import java.nio.file.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Controller
public class MyController {
    
    private final AdminService adminService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final AssignmentService assignmentService;
    
    private SubmissionRepository submissionRepository;
    private final QuestionService questionService;

    
    private final SubmissionService submissionService;
    
    
    public MyController(AdminService adminService, StudentService studentService,
                      CourseService courseService, EnrollmentService enrollmentService,
                      AssignmentService assignmentService,SubmissionService submissionService,SubmissionRepository submissionRepository, QuestionService questionService) {
        this.adminService = adminService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.assignmentService = assignmentService;
        this.submissionService=submissionService;
        this.questionService=questionService;
        this.submissionRepository=submissionRepository;
       
    }

    @GetMapping("/index")
    public String homePage() {
        return "index";
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
       session.invalidate();
       return "redirect:/index";
    }

    @GetMapping("/index/admin")
    public String adminLogin(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin";
    }
    
    @PostMapping("/index/admin")
    public String loginAdmin(@ModelAttribute("admin") Admin admin, Model model, HttpSession session) {
        Admin existingAdmin = adminService.findByAdminNameAndPassword(admin.getAdminName(), admin.getPassword());
        if(existingAdmin != null) {
            session.setAttribute("admin", existingAdmin);
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "admin";
        }
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/index/admin";
        
        model.addAttribute("admin", admin);
        model.addAttribute("studentCount", studentService.getAllStudents().size());
        model.addAttribute("courseCount", courseService.getAllCourses().size());
        model.addAttribute("totalEnrollments", enrollmentService.getAllEnrollments().size());
        
        return "admin-dashboard";
    }
    
    @PostMapping("/admin/enrollments/delete/{id}")
    public String deleteEnrollment(@PathVariable Long id, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/index/admin";
        
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        if (enrollment == null) {
            throw new ResourceNotFoundException("Enrollment not found");
        }
        
        Long courseId = enrollment.getCourse().getId();
        enrollmentService.deleteEnrollment(id);
        
        return "redirect:/admin/courses/" + courseId + "/students";
    }

    @GetMapping("/index/admin-signup")
    public String adminSignup(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin-signup";
    }
    
    @PostMapping("/index/admin-signup")
    public String saveSignup(@ModelAttribute("admin") Admin admin) {
        adminService.saveLogin(admin);
        return "redirect:/index/admin";
    }

    @GetMapping("/admin/courses")
    public String adminCourses(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/index/admin";
        
        model.addAttribute("courses", courseService.getAllCourses());
        return "admin-courses";
    }
    @GetMapping("/admin/courses/new")
    public String createCourseForm(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/index/admin";
        
        model.addAttribute("course", new Course());
        return "course-form";
    }

    @PostMapping("/admin/courses/save")
    public String saveCourse(@ModelAttribute Course course, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/index/admin";
        
        course.setCreatedBy(admin);
        course.setCreatedAt(LocalDateTime.now());
        courseService.createCourse(course); // Make sure this saves to database
        return "redirect:/admin/courses";
    }
    @GetMapping("/admin/courses/{courseId}/assignments")
    public String viewAssignments(@PathVariable Long courseId, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/index/admin";
        
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found");
        }
        
        List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("assignments", assignments);
        return "admin-assignments";
    }

    @GetMapping("/admin/courses/{courseId}/assignments/new")
    public String createAssignmentForm(@PathVariable Long courseId, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/index/admin";
        
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found");
        }
        
        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        
        for (int i = 0; i < 4; i++) {
            assignment.getAnswerOptions().add(new AnswerOption());
        }
        
        model.addAttribute("assignment", assignment);
        return "assignment-form";
    }
    
    @PostMapping("/admin/assignments/{assignmentId}/questions/save")
    public String saveQuestion(@PathVariable Long assignmentId,
                               @RequestParam String questionText,
                               @RequestParam List<String> options,
                               @RequestParam int correctIndex) {
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);

        Question question = new Question();
        question.setQuestionText(questionText);
        question.setAssignment(assignment);
        question.setCorrectAnswer(options.get(correctIndex));

        List<AnswerOption> answerOptions = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            AnswerOption opt = new AnswerOption();
            opt.setOptionText(options.get(i));
            opt.setQuestion(question);
            answerOptions.add(opt);
        }

        question.setOptions(answerOptions);
        questionService.save(question); // Assume service exists

        return "redirect:/admin/assignments/" + assignmentId + "/questions/new";
    }
    @PostMapping("/admin/courses/{courseId}/assignments/save")
    public String saveAssignment(
        @PathVariable Long courseId,
        @RequestParam String assignmentTitle,
        @RequestParam String questionText,         // renamed for clarity
        @RequestParam List<String> optionTexts,
        @RequestParam Integer correctAnswerIndex,
        HttpSession session,
        RedirectAttributes redirectAttributes) {

        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/index/admin";

        try {
            Course course = courseService.getCourseById(courseId);

            Assignment assignment = new Assignment();
            assignment.setAssignmentTitle(assignmentTitle);
            assignment.setCourse(course);

            Question question = new Question();
            question.setQuestionText(questionText);
            question.setAssignment(assignment);

            List<AnswerOption> answerOptions = new ArrayList<>();
            for (int i = 0; i < optionTexts.size(); i++) {
                String optionText = optionTexts.get(i);
                if (!optionText.isEmpty()) {
                    AnswerOption option = new AnswerOption();
                    option.setOptionText(optionText);
                    option.setQuestion(question);   
                    answerOptions.add(option);

                    if (i == correctAnswerIndex) {
                        question.setCorrectAnswer(optionText); 
                    }
                }
            }
            question.setOptions(answerOptions);

            questionService.save(question);

            List<Question> questions = new ArrayList<>();
            questions.add(question);
            assignment.setQuestions(questions);

            assignmentService.saveAssignment(assignment);

            redirectAttributes.addFlashAttribute("success", "Assignment created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create assignment");
        }

        return "redirect:/admin/courses/" + courseId + "/assignments";
    }

    
    @GetMapping("/admin/assignments/{assignmentId}/submissions")
    public String viewSubmissions(@PathVariable Long assignmentId, Model model) {
    	Assignment assignment = assignmentService.findById(assignmentId)
    .orElseThrow(() -> new IllegalArgumentException("Invalid assignment ID"));



        List<Submission> submissions = submissionService.getSubmissionsByAssignment(assignment);

        model.addAttribute("assignment", assignment);
        model.addAttribute("submissions", submissions);
        return "assignment-submissions";
    }
    @GetMapping("/admin/courses/{courseId}/students")
    public String viewEnrolledStudents(@PathVariable Long courseId, Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/index/admin";
        
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            throw new ResourceNotFoundException("Course not found");
        }
        
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
        model.addAttribute("course", course);
        model.addAttribute("enrollments", enrollments);
        return "admin-course-students";
    }
    
    @GetMapping("/admin/submissions")
    public String viewAllSubmissions(Model model) {
        List<Submission> submissions = submissionService.findAll();
        model.addAttribute("submissions", submissions);
        return "admin-submissions";
    }

    
    
    @GetMapping("/admin/students")
    public String adminStudents(Model model, HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return "redirect:/index/admin";
        
        model.addAttribute("students", studentService.getAllStudents());
        return "admin-students";
    }
    
  

  
    
    @GetMapping("/admin/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/admin/students";
    }
    
    

@GetMapping("/admin/students/resume/{studentId}")
public ResponseEntity<Resource> viewResume(@PathVariable Long studentId) throws IOException {
    StudentPortal student = studentService.getStudentById(studentId);
    if (student == null || student.getResumePath() == null) {
        throw new ResourceNotFoundException("Resume not found");
    }

    Path filePath = Paths.get("uploads/" + student.getResumePath()).normalize();
    Resource resource = new UrlResource(filePath.toUri());

    if (!resource.exists()) {
        throw new ResourceNotFoundException("Resume file not found");
    }

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
}

@GetMapping("/admin/students/edit/{id}")
public String editStudentForm(@PathVariable Long id, Model model, HttpSession session) {
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) return "redirect:/index/admin";
    
    StudentPortal student = studentService.getStudentById(id);
    if (student == null) {
        throw new ResourceNotFoundException("Student not found");
    }
    
    model.addAttribute("student", student);
    return "student-edit-form";
}

@PostMapping("/admin/students/update/{id}")
public String updateStudent(@PathVariable Long id, 
                          @ModelAttribute("student") StudentPortal student,
                          @RequestParam(value = "resume", required = false) MultipartFile file,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) return "redirect:/index/admin";
    
    try {
        StudentPortal existingStudent = studentService.getStudentById(id);
        
        if (file != null && !file.isEmpty()) {
            if (existingStudent.getResumePath() != null) {
                Path oldFilePath = Paths.get("uploads/" + existingStudent.getResumePath());
                Files.deleteIfExists(oldFilePath);
            }
            
            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + fileName), 
                    StandardCopyOption.REPLACE_EXISTING);
            }
            
            student.setResumePath(fileName);
        } else {
            student.setResumePath(existingStudent.getResumePath());
        }
        
        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("success", "Student updated successfully!");
    } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("error", "Failed to update student");
    }
    
    return "redirect:/admin/students";
}

    @GetMapping("/index/student")
    public String studentLogin(Model model) {
        model.addAttribute("student", new StudentPortal());
        return "student";
    }

    @PostMapping("/index/student")
    public String loginStudent(@ModelAttribute("student") StudentPortal student, Model model, HttpSession session) {
        StudentPortal existingStudent = studentService.findByEmailAndPassword(
            student.getEmail(), student.getPassword());
        
        if(existingStudent != null) {
            session.setAttribute("student", existingStudent);
            return "redirect:/student/dashboard";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "student";
        }
    }
    
    @GetMapping("/student/dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        StudentPortal student = (StudentPortal) session.getAttribute("student");
        if (student == null) return "redirect:/index/student";
        
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(student.getId());
        model.addAttribute("student", student);
        model.addAttribute("enrollments", enrollments);
        return "student-dashboard";
    }
    
    

    @PostMapping("/student/courses/{enrollmentId}/unenroll")
    public String unenrollCourse(@PathVariable Long enrollmentId, 
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        StudentPortal student = (StudentPortal) session.getAttribute("student");
        if (student == null) return "redirect:/index/student";
        
        try {
            Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);
            // Verify the enrollment belongs to the current student
            if (enrollment != null && enrollment.getStudent().getId().equals(student.getId())) {
                enrollmentService.deleteEnrollment(enrollmentId);
                redirectAttributes.addFlashAttribute("success", "Successfully unenrolled from course");
            } else {
                redirectAttributes.addFlashAttribute("error", "Enrollment not found or not authorized");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error unenrolling from course");
            e.printStackTrace();
        }
        
        return "redirect:/student/courses";
    }

    @GetMapping("/student/courses/{courseId}/edit")
    public String editEnrollmentForm(@PathVariable Long courseId,
                                   Model model,
                                   HttpSession session) {
        StudentPortal student = (StudentPortal) session.getAttribute("student");
        if (student == null) return "redirect:/index/student";
        
        Enrollment enrollment = enrollmentService.getEnrollmentByStudentAndCourse(student.getId(), courseId);
        if (enrollment == null) {
            return "redirect:/student/courses";
        }
        
        model.addAttribute("enrollment", enrollment);
        return "edit-enrollment"; 
    }

    @PostMapping("/student/courses/{enrollmentId}/update")
    public String updateEnrollment(@PathVariable Long enrollmentId,
                                 @ModelAttribute Enrollment enrollment,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        StudentPortal student = (StudentPortal) session.getAttribute("student");
        if (student == null) return "redirect:/index/student";
        
        try {
            Enrollment existingEnrollment = enrollmentService.getEnrollmentById(enrollmentId);

            if (existingEnrollment != null && existingEnrollment.getStudent().getId().equals(student.getId())) {
                existingEnrollment.setEnrollmentDate(enrollment.getEnrollmentDate());
                enrollmentService.enrollStudent(existingEnrollment);
                redirectAttributes.addFlashAttribute("success", "Enrollment updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Enrollment not found or not authorized");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating enrollment");
            e.printStackTrace();
        }
        
        return "redirect:/student/courses";
    }
    
    @GetMapping("/index/student-signup")
    public String studentSignup(Model model) {
        model.addAttribute("student", new StudentPortal());
        return "student-signup";
    }

    @PostMapping("/index/student-signup")
    public String saveStudent(@ModelAttribute("student") StudentPortal student,
                            @RequestParam("resume") MultipartFile file,
                            RedirectAttributes redirectAttributes) {
        
        if (!file.isEmpty()) {
            try {
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + fileName), 
                        StandardCopyOption.REPLACE_EXISTING);
                }
                
                student.setResumePath(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Failed to upload resume");
                return "redirect:/index/student-signup";
            }
        } else {
           student.setResumePath(null);
        }
   
        try {
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("success", "Registration successful!");
            return "redirect:/index/student";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Registration failed");
            return "redirect:/index/student-signup";
        }
    }
    
    @GetMapping("/student/courses")
    public String viewStudentCourses(Model model, HttpSession session) {
        StudentPortal student = (StudentPortal) session.getAttribute("student");

        if (student == null) return "redirect:/index/student";

        List<Course> allCourses = courseService.getAllCourses();

        List<Enrollment> enrolledCourses = enrollmentService.getEnrollmentsByStudent(student.getId());

        model.addAttribute("allCourses", allCourses);
        model.addAttribute("enrolledCourses", enrolledCourses);

        return "student-courses"; 
    }

    
    @PostMapping("/student/courses/enroll")
    public String enrollCourse(@RequestParam Long courseId, HttpSession session) {
        StudentPortal student = (StudentPortal) session.getAttribute("student");
        if (student == null) return "redirect:/index/student";
        
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(courseService.getCourseById(courseId));
        enrollment.setEnrollmentDate(LocalDate.now());
        
        enrollmentService.enrollStudent(enrollment);
        return "redirect:/student/courses";
    }

    @GetMapping("/student/courses/{courseId}/assignments")
    public String studentAssignments(@PathVariable Long courseId, HttpSession session, Model model) {
        StudentPortal student = (StudentPortal) session.getAttribute("student");
        if (student == null) return "redirect:/index/student";

        Course course = courseService.getCourseById(courseId);
        List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);

        List<AssignmentStatusDTO> assignmentStatuses = new ArrayList<>();
        for (Assignment assignment : assignments) {
            Optional<Submission> submissionOpt = submissionRepository.findTopByAssignmentAndStudentOrderByIdDesc(assignment, student);
            Submission submission = submissionOpt.orElse(null);
            assignmentStatuses.add(new AssignmentStatusDTO(assignment, submission));
        }

        model.addAttribute("course", course);
        model.addAttribute("assignments", assignments);
        model.addAttribute("assignmentStatuses", assignmentStatuses);
        return "student-assignments";
    }

    @GetMapping("/student/assignments")
    public String viewAllAssignments(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        StudentPortal student = (StudentPortal) session.getAttribute("student");

        if (student == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to view assignments.");
            return "redirect:/student/dashboard"; 
        }

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(student.getId());
        List<Assignment> assignments = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
            Course course = enrollment.getCourse();
            assignments.addAll(assignmentService.getAssignmentsByCourse(course));
        }

        model.addAttribute("assignments", assignments);
        return "student-assignments"; 
    }




    @GetMapping("/student/assignments/{assignmentId}")
    public String viewAssignment(@PathVariable Long assignmentId, Model model, HttpSession session) {
        StudentPortal student = (StudentPortal) session.getAttribute("student");
        if (student == null) return "redirect:/index/student";
        
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        model.addAttribute("assignment", assignment);
        return "student-assignment-view";
    }
    
    @PostMapping("/student/assignments/{id}/submit")
    public String submitAssignment(
            @PathVariable("id") Long assignmentId,
            @RequestParam("selectedAnswer") String selectedAnswer,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        StudentPortal student = (StudentPortal) session.getAttribute("student");
        if (student == null) {
            return "redirect:/student/dashboard";  
        }

        try {
            submissionService.saveSubmission(assignmentId, student.getId(), selectedAnswer);
            redirectAttributes.addFlashAttribute("successMessage", "Submission successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Submission failed: " + e.getMessage());
        }

        return "redirect:/student/courses"; 
    }


    @PostMapping("/student/assignments/{assignmentId}/submit")
    public String submitAssignment(@PathVariable Long assignmentId,
                                   @RequestParam Map<String, String> allParams,
                                   HttpSession session, RedirectAttributes redirectAttributes) {
        StudentPortal student = (StudentPortal) session.getAttribute("student");

        Submission submission = new Submission();
        submission.setAssignment(assignmentService.getAssignmentById(assignmentId));
        submission.setStudent(student);

        List<StudentAnswer> answerList = new ArrayList<>();
        int correct = 0, wrong = 0;

        for (String key : allParams.keySet()) {
            if (key.startsWith("question_")) {
                Long questionId = Long.parseLong(key.replace("question_", ""));
                String selectedAnswer = allParams.get(key);

                Question question = questionService.findById(questionId);
                boolean isCorrect = question.getCorrectAnswer().equals(selectedAnswer);

                if (isCorrect) correct++; else wrong++;

                StudentAnswer sa = new StudentAnswer();
                sa.setQuestion(question);
                sa.setSelectedAnswer(selectedAnswer);
                sa.setSubmission(submission);
                answerList.add(sa);
            }
        }

        submission.setAnswers(answerList);
        submission.setCorrectCount(correct);
        submission.setWrongCount(wrong);
        submissionService.save(submission);

        redirectAttributes.addFlashAttribute("results", answerList);
        redirectAttributes.addFlashAttribute("total", correct + wrong);
        redirectAttributes.addFlashAttribute("correct", correct);
        redirectAttributes.addFlashAttribute("wrong", wrong);

        return "redirect:/student/assignments/" + assignmentId + "/result";
    }

    @GetMapping("/student/assignments/{assignmentId}/result")
    public String viewResult(@PathVariable Long assignmentId,
                             Principal principal,
                             HttpSession session,
                             Model model) {

        StudentPortal student = (StudentPortal) session.getAttribute("student");

        Submission submission = submissionService.findByAssignmentAndStudent(assignmentId, student.getId());

        if (submission == null) {
            return "redirect:/student/assignments/" + assignmentId;
        }

        model.addAttribute("submission", submission);
        model.addAttribute("questions", questionService.findByAssignment(submission.getAssignment()));

        return "assignment-result";
    }
}