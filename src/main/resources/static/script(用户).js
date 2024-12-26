// 获取用户罚金信息并显示
function viewFines() {
    // 假设你通过当前用户的 ID 来查询罚金信息
    const userId = 'user123'; // 替换为当前用户的实际 ID
    fetch(`/api/getUserFines?userId=${userId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 设置罚金总额并显示弹窗
                document.getElementById('fine-amount').textContent = `${data.message}元`;
                document.getElementById('fine-modal').style.display = 'block';
            } else {
                alert('无法获取罚金信息');
            }
        })
        .catch(error => {
            console.error('获取罚金信息失败:', error);
            alert('发生错误，无法获取罚金信息');
        });
}

// 关闭罚金弹窗
function closeFineModal() {
    document.getElementById('fine-modal').style.display = 'none';
}

// 点击缴纳罚金按钮
function payFine() {
    const fineAmount = document.getElementById('fine-amount').textContent.replace('元', '');
    if (fineAmount > 0) {
        // 发送请求到后端支付罚金
        const userId = 'user123'; // 当前用户 ID
        fetch('/api/payFine', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userId: userId,
                amount: fineAmount
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('支付成功');
                window.location.href = '/fines';  // 跳转到相应页面
            } else {
                alert('支付失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('支付失败:', error);
            alert('发生错误，无法支付罚金');
        });
    } else {
        alert('没有罚金需要支付');
    }
}