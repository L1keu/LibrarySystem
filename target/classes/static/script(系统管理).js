// 备份数据
function backupData() {
    fetch('/api/backupData', {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('数据备份成功');
        } else {
            alert('数据备份失败');
        }
    })
    .catch(error => {
        console.error('备份失败:', error);
        alert('发生错误，无法备份数据');
    });
}

// 恢复数据
function restoreData() {
    fetch('/api/restoreData', {
        method: 'POST'
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('数据恢复成功');
        } else {
            alert('数据恢复失败');
        }
    })
    .catch(error => {
        console.error('恢复失败:', error);
        alert('发生错误，无法恢复数据');
    });
}

// 获取系统监控信息
function getSystemInfo() {
    fetch('/api/systemInfo')
    .then(response => response.json())
    .then(data => {
        document.getElementById('user-count').textContent = data.userCount;
        document.getElementById('borrow-count').textContent = data.borrowCount;
        document.getElementById('system-health').textContent = data.systemHealth;
    })
    .catch(error => {
        console.error('获取系统信息失败:', error);
    });
}

// 修改用户角色
function changeRole(username) {
    const newRole = prompt('请输入新角色（普通用户/管理员）：');
    if (newRole) {
        fetch('/api/changeRole', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: username, newRole: newRole })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('角色修改成功');
                document.getElementById(`role-${username}`).textContent = newRole;
            } else {
                alert('角色修改失败');
            }
        })
        .catch(error => {
            console.error('角色修改失败:', error);
        });
    }
}

// 页面加载时获取系统信息
document.addEventListener('DOMContentLoaded', function() {
    getSystemInfo();
});