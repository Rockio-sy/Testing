// ===== AUTH FUNCTIONS =====
const API_BASE_URL = 'http://localhost:8080'; // Update if needed

/**
 * Handles login form submission.
 * Saves role to localStorage on success.
 */

async function handleLogin(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorElement = document.getElementById('errorMessage');

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username: email, password}) // Adjust fields if LoginForm DTO differs
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Login failed');
        }

        const role = await response.text(); // Get role from response
        localStorage.setItem('userRole', role); // Save role for later

// Redirect based on role
        switch (role) {
            case 'ADMIN':
                window.location.href = '/admin-dashboard.html';
                break;
            case 'TEACHER':
                window.location.href = '/teacher-dashboard.html';
                break;
            case 'STUDENT':
                window.location.href = '/student-dashboard.html';
                break;
            default:
                throw new Error('Unknown role detected');
        }

    } catch (error) {
        errorElement.textContent = error.message;
        errorElement.classList.remove('hidden');
    }
}

document.getElementById('loginForm')?.addEventListener('submit', handleLogin);

// ===== TASK FUNCTIONS =====
const TASK_API_URL = `${API_BASE_URL}/task`;

/**
 * Fetches all tasks for student dashboard
 */
async function loadTasks() {
    try {
        const response = await fetch(`${TASK_API_URL}/all`);
        if (!response.ok) throw new Error('Failed to load tasks');
        return await response.json();
    } catch (error) {
        console.error('Task loading error:', error);
        return [];
    }
}

/**
 * Renders tasks as a list
 */
function renderTaskList(tasks) {
    const container = document.getElementById('taskList');
    if (!container) return "NOTHING";

    container.innerHTML = tasks.map(task => `
        <div class="list-item">
            <div>
                <h4>${task.title || 'Untitled Task'}</h4>
                <p>${task.description ? task.description.substring(0, 50) + '...' : 'No description'}</p>
            </div>
            <a href="/task-details.html?id=${task.id}" class="btn btn-outline">View Details</a>
        </div>
    `).join('');
}

/**
 * Loads task details, submission status, and grade
 */
async function loadTaskDetails() {
    const urlParams = new URLSearchParams(window.location.search);
    const taskId = urlParams.get('id');
    if (!taskId) return;

    const taskDetailsElement = document.getElementById('taskDetails');
    if (!taskDetailsElement) return;

    try {
        // 1. Load Task Details
        const taskResponse = await fetch(`${TASK_API_URL}/one?id=${taskId}`);
        if (!taskResponse.ok) throw new Error('Task not found');
        const task = await taskResponse.json();

        // 2. Load Submission
        let submissionHtml = '<div class="mb-3"><h3>Submission Status</h3>';
        try {
            const submissionResponse = await fetch(`${API_BASE_URL}/submit/by-task?taskId=${taskId}`);
            if (submissionResponse.ok) {
                const submission = await submissionResponse.json();
                submissionHtml += `
                    <p><strong>Submitted:</strong> Yes</p>
                    <p><strong>Your Answer:</strong></p>
                    <div class="card p-2">${submission.answer}</div>
                `;

                // 3. Load Grade if submission exists
                try {
                    const gradeResponse = await fetch(`${API_BASE_URL}/grade/one?id=${submission.id}`);
                    if (gradeResponse.ok) {
                        const grade = await gradeResponse.json();
                        submissionHtml += `
                            <div class="mt-3">
                                <h3>Grading</h3>
                                <p><strong>Grade:</strong> ${grade.value}</p>
                                <p><strong>Feedback:</strong></p>
                                <div class="card p-2">${grade.feedback}</div>
                            </div>
                        `;
                    } else {
                        submissionHtml += '<p class="mt-3"><strong>Grade:</strong> No grade yet</p>';
                    }
                } catch (gradeError) {
                    submissionHtml += '<p class="mt-3"><strong>Grade:</strong> Error loading grade</p>';
                }
            } else {
                submissionHtml += '<p>Not submitted yet</p>';
            }
        } catch (submissionError) {
            submissionHtml += '<p>Error checking submission status</p>';
        }
        submissionHtml += '</div>';

        // Combine all data
        taskDetailsElement.innerHTML = `
            <div class="card-header">
                <h2>${task.title}</h2>
            </div>
            <div>
                <h3>Task Description</h3>
                <p>${task.description}</p>
                ${submissionHtml}
                <div class="mt-3">
                    <a href="/student-dashboard.html" class="btn btn-primary">Back to Tasks</a>
                </div>
            </div>
        `;

    } catch (taskError) {
        taskDetailsElement.innerHTML = `
            <div class="text-center" style="color: var(--danger-color);">
                <p>${taskError.message}</p>
                <a href="/student-dashboard.html" class="btn btn-primary">Back to Tasks</a>
            </div>
        `;
    }
}

// ===== INITIALIZERS =====
// For student dashboard
if (window.location.pathname.includes('student-dashboard')) {
    loadTasks().then(renderTaskList);
}

// For task details page
if (window.location.pathname.includes('task-details')) {
    loadTaskDetails();
}

// ===== SUBMISSION FUNCTIONS =====
async function loadTasksForDropdown() {
    try {
        const response = await fetch(`${TASK_API_URL}/all`);
        if (!response.ok) throw new Error('Failed to load tasks');
        const tasks = await response.json();

        const select = document.getElementById('taskSelect');
        if (select) {
            select.innerHTML = tasks.map(task =>
                `<option value="${task.id}">${task.title}</option>`
            ).join('');
        }
    } catch (error) {
        console.error('Task dropdown error:', error);
    }
}

async function validateUsername(username) {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/get-id?username=${username}`);
        if (!response.ok) return null;
        const data = await response.json();
        return data.id || null;
    } catch (error) {
        return null;
    }
}

async function handleSubmission(event) {
    event.preventDefault();

    const username = document.getElementById('username').value.trim();
    const teacherUsername = document.getElementById('teacherUsername').value.trim();
    const taskId = document.getElementById('taskSelect').value;
    const answer = document.getElementById('answer').value.trim();

    const userError = document.getElementById('usernameError');
    const teacherError = document.getElementById('teacherError');

    // Validate student
    const studentId = await validateUsername(username);
    if (!studentId) {
        userError.textContent = 'Invalid student username.';
        userError.classList.remove('hidden');
        return;
    } else {
        userError.classList.add('hidden');
    }

    // Validate teacher
    const teacherId = await validateUsername(teacherUsername);
    if (!teacherId) {
        teacherError.textContent = 'Invalid teacher username.';
        teacherError.classList.remove('hidden');
        return;
    } else {
        teacherError.classList.add('hidden');
    }

    try {
        const submissionData = {
            teacherId,
            studentId,
            taskId,
            answer
        };

        const response = await fetch(`${API_BASE_URL}/submit/new`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(submissionData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            if (errorText.includes('already exists')) {
                alert('You have already submitted this task.');
            } else {
                throw new Error('Submission failed');
            }
            return;
        }
    } catch (error) {
        alert(`Error: ${error.message}`);
    }

}


// Initialize submission page
if (window.location.pathname.includes('submit-task')) {
    loadTasksForDropdown();
    document.getElementById('submitForm')?.addEventListener('submit', handleSubmission);
}

// ===== TEACHER FUNCTIONS =====
const SUBMISSION_API_URL = `${API_BASE_URL}/submit`;

/**
 * Fetches all submissions
 */
async function fetchSubmissions() {
    try {
        const response = await fetch(`${SUBMISSION_API_URL}/all`);
        if (!response.ok) throw new Error('Failed to load submissions');
        return await response.json();
    } catch (error) {
        console.error('Submission loading error:', error);
        return [];
    }
}

/**
 * Renders submissions list
 */
function renderSubmissions(submissions) {
    const container = document.getElementById('submissionList');
    if (!container) return;

    container.innerHTML = submissions.map(sub => `
        <div class="list-item">
            <div>
                <h4>Submission #${sub.id.slice(0, 8)}</h4>
                <p>Task: ${sub.taskId.slice(0, 8)} | Student: ${sub.studentId.slice(0, 8)}</p>
            </div>
            <a href="/submission-details.html?id=${sub.id}" class="btn btn-outline">View</a>
        </div>
    `).join('');
}

// Initialize teacher dashboard
if (window.location.pathname.includes('teacher-dashboard')) {
    fetchSubmissions().then(renderSubmissions);
}


// ===== GRADE MANAGEMENT =====
let currentSubmissionId = null;

function showGradingForm() {
    document.getElementById('gradingForm').classList.remove('hidden');
    document.getElementById('gradeBtn').classList.add('hidden');
}

function hideGradingForm() {
    document.getElementById('gradingForm').classList.add('hidden');
    document.getElementById('gradeBtn').classList.remove('hidden');
    document.getElementById('gradeForm').reset();
}

async function submitGrade(event) {
    event.preventDefault();

    const gradeValue = document.getElementById('gradeValue').value;
    const feedback = document.getElementById('feedback').value;

    try {
        const response = await fetch(`${API_BASE_URL}/grade/new`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                value: parseFloat(gradeValue),
                feedback: feedback,
                submissionId: currentSubmissionId
            })
        });

        if (!response.ok) throw new Error('Grading failed');

        alert('Grade submitted successfully!');
        hideGradingForm();
        // Refresh submission details
        loadSubmissionDetails();
    } catch (error) {
        alert(`Error: ${error.message}`);
    }
}

// Update loadSubmissionDetails to store ID
async function loadSubmissionDetails() {
    const urlParams = new URLSearchParams(window.location.search);
    currentSubmissionId = urlParams.get('id');
    if (!currentSubmissionId) return;

    const container = document.getElementById('submissionDetails');
    if (!container) return;

    try {
        // 1. Fetch all submissions
        const response = await fetch(`${SUBMISSION_API_URL}/all`);
        if (!response.ok) throw new Error('Failed to load submissions list');
        const submissions = await response.json();

        // 2. Find submission by ID
        const submission = submissions.find(sub => sub.id === currentSubmissionId);
        if (!submission) {
            container.innerHTML = `<p style="color: var(--danger-color);">Submission not found.</p>`;
            return;
        }

        // 3. Fetch student username
        const studentUsername = await fetchUsername(submission.studentId);

        // 4. Fetch teacher username
        const teacherUsername = await fetchUsername(submission.teacherId);

        // 5. Fetch task title
        const taskTitle = await fetchTaskTitle(submission.taskId);

        // 6. Render with readable info
        container.innerHTML = `
            <div class="card-header">
                <h2>Submission Details</h2>
            </div>
            <div>
                <p><strong>Task:</strong> ${taskTitle}</p>
                <p><strong>Student:</strong> ${studentUsername}</p>
                <p><strong>Teacher:</strong> ${teacherUsername}</p>
                <p><strong>Answer:</strong></p>
                <div class="card p-2">${submission.answer}</div>
            </div>
        `;

        const grade = await fetchGrade(currentSubmissionId);

        if (grade) {
            container.innerHTML += `
        <div class="mt-3">
            <h3>Grade</h3>
            <p><strong>Value:</strong> ${grade.value}</p>
            <p><strong>Feedback:</strong></p>
            <div class="card p-2">${grade.feedback}</div>
        </div>
    `;

            // Hide grading button if grade already exists
            document.getElementById('gradeBtn')?.classList.add('hidden');
        }
    } catch (error) {
        container.innerHTML = `
            <p style="color: var(--danger-color);">Error: ${error.message}</p>
        `;
    }
}

async function fetchUsername(userId) {
    try {
        const res = await fetch(`${API_BASE_URL}/admin/get-username?id=${userId}`);
        if (!res.ok) throw new Error();
        return await res.text(); // since it's plain text response
    } catch {
        return 'Unknown User';
    }
}

async function fetchTaskTitle(taskId) {
    try {
        const res = await fetch(`${API_BASE_URL}/task/title?id=${taskId}`);
        if (!res.ok) throw new Error();
        return await res.text(); // plain text title
    } catch {
        return 'Unknown Task';
    }
}

// Initialize submission details page
if (window.location.pathname.includes('submission-details')) {
    loadSubmissionDetails();

    // Button event listeners
    document.getElementById('backBtn')?.addEventListener('click', () => {
        window.location.href = '/teacher-dashboard.html';
    });

    document.getElementById('gradeBtn')?.addEventListener('click', showGradingForm);
    document.getElementById('cancelGrade')?.addEventListener('click', hideGradingForm);
    document.getElementById('gradeForm')?.addEventListener('submit', submitGrade);
}

async function fetchGrade(submissionId) {
    try {
        const res = await fetch(`${API_BASE_URL}/grade/one?id=${submissionId}`);
        if (!res.ok) throw new Error();
        return await res.json();
    } catch {
        return null;
    }
}

// ===== ADMIN FUNCTIONS =====
const ADMIN_API_URL = `${API_BASE_URL}/admin`;

/**
 * Fetches all users
 */
async function fetchUsers() {
    try {
        const response = await fetch(`${ADMIN_API_URL}/all`);
        if (!response.ok) throw new Error('Failed to load users');
        return await response.json();
    } catch (error) {
        console.error('User loading error:', error);
        return [];
    }
}

/**
 * Deletes a user
 */
async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user?')) return;

    try {
        const response = await fetch(`${ADMIN_API_URL}/delete?id=${userId}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Deletion failed');

        // Refresh the list after deletion
        loadUsers();
    } catch (error) {
        alert(`Error: ${error.message}`);
    }
}

/**
 * Renders users list with delete buttons
 */
function renderUsers(users) {
    const container = document.getElementById('userList');
    if (!container) return;

    container.innerHTML = users.map(user => `
        <div class="list-item">
            <div>
                <h4>${user.username || 'Unnamed User'}</h4>
                <p>Role: ${user.role} | ID: ${user.id.slice(0, 8)}</p>
            </div>
            <button onclick="deleteUser('${user.id}')" class="btn btn-danger">Delete</button>
        </div>
    `).join('');
}

/**
 * Loads users into the list
 */
async function loadUsers() {
    const users = await fetchUsers();
    renderUsers(users);
}

// Initialize admin dashboard
if (window.location.pathname.includes('admin-dashboard')) {
    loadUsers();

    document.getElementById('createUserBtn')?.addEventListener('click', () => {
        // Will implement later when you provide the form details
        alert('Create user functionality will be added next');
    });
}

// Make deleteUser available globally
window.deleteUser = deleteUser;


// ===== USER CREATION =====
async function createUser(event) {
    event.preventDefault();

    const formData = {
        username: document.getElementById('username').value.trim(),
        password: document.getElementById('password').value.trim(),
        fullName: document.getElementById('fullName').value.trim(),
        role: document.getElementById('role').value
    };

    const errorElement = document.getElementById('errorMessage');
    errorElement.classList.add('hidden');

    // Simple frontend validation
    if (!formData.role) {
        showError(errorElement, 'Please select a role');
        return;
    }

    try {
        const response = await fetch(`${ADMIN_API_URL}/new`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Creation failed');
        }

        alert('User created successfully!');
        window.location.href = '/admin-dashboard.html';
    } catch (error) {
        showError(errorElement, error.message);
    }
}

function showError(element, message) {
    element.textContent = message;
    element.classList.remove('hidden');
}

// Initialize create user page
if (window.location.pathname.includes('create-user')) {
    document.getElementById('userForm')?.addEventListener('submit', createUser);
}

///////////////////////////////// HERE IS THE FRONT FOR FAQ /////////////////////////////////
