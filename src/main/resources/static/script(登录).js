// 切换登录与注册表单
function toggleForm(formType) {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    if (formType === 'register') {
        loginForm.style.display = 'none';
        registerForm.style.display = 'block';
    } else {
        loginForm.style.display = 'block';
        registerForm.style.display = 'none';
    }
}

// 登录请求
function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById('login-username').value;
    const password = document.getElementById('login-password').value;

    // 发送请求到后端
    fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            username: username,
            password: password,
        }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('登录成功！');
            // 登录成功，根据用户身份跳转
            if (data.role === 'user') {
                window.location.href = '首页(用户).html';  // 普通用户页面
            } else if (data.role === 'admin') {
                window.location.href = '首页(管理员).html';  // 管理员页面
            }
        } else {
            alert('登录失败：' + data.message);
        }
    })
    .catch(error => {
        console.error('登录请求失败：', error);
        alert('登录失败，请稍后再试！');
    });
}

// 注册请求
function handleRegister(event) {
    event.preventDefault();

    const role = document.getElementById('register-role').value;
    const name = document.getElementById('register-name').value;
    const contact = document.getElementById('register-contact').value;
    const email = document.getElementById('register-email').value;
    const username = document.getElementById('register-username').value;
    const password = document.getElementById('register-password').value;
    const confirmPassword = document.getElementById('register-confirm-password').value;

    if (password !== confirmPassword) {
        alert('密码与确认密码不匹配！');
        return;
    }

    // 发送请求到后端
    fetch('/api/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            role: role,
            name: name,
            contact: contact,
            email: email,
            username: username,
            password: password,
        }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('注册成功！请登录');
            toggleForm('login');
        } else {
            alert('注册失败：' + data.message);
        }
    })
    .catch(error => {
        console.error('注册请求失败：', error);
        alert('注册失败，请稍后再试！');
    });
}