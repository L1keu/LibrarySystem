// 获取当前用户信息并显示在页面上
document.addEventListener('DOMContentLoaded', function() {
    fetchUserInfo();
});

// 从后端获取用户信息
function fetchUserInfo() {
    const userId = 'exampleUserId'; // 需要在前端获取当前用户ID，可能来自登录信息

    fetch(`/api/getUserInfo?userId=${userId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                document.getElementById('username').textContent = data.data.username;
                document.getElementById('email').textContent = data.data.email;
                document.getElementById('phone').textContent = data.data.phone;
            } else {
                alert('获取用户信息失败');
            }
        })
        .catch(error => {
            console.error('获取用户信息失败:', error);
            alert('发生错误，无法获取用户信息');
        });
}

// 提交修改的用户信息
document.getElementById('edit-form').addEventListener('submit', function(event) {
    event.preventDefault();  // 阻止表单默认提交行为

    // 获取表单数据
    const newEmail = document.getElementById('new-email').value;
    const newPhone = document.getElementById('new-phone').value;
    const newPassword = document.getElementById('new-password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    // 验证新密码和确认密码是否一致
    if (newPassword !== confirmPassword) {
        alert('新密码和确认密码不一致');
        return;
    }

    const userId = 'exampleUserId'; // 需要在前端获取当前用户ID

    // 发送更新请求到后端
    fetch(`/api/updateUserInfo?userId=${userId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: newEmail,
            phone: newPhone,
            password: newPassword
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('个人信息修改成功');
            fetchUserInfo();  // 重新获取用户信息并更新页面
        } else {
            alert(data.message || '修改失败');
        }
    })
    .catch(error => {
        console.error('修改失败:', error);
        alert('发生错误，修改失败');
    });
});