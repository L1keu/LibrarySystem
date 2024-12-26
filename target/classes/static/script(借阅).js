// 修改获取借阅记录的请求
// 获取借阅记录并展示
function fetchBorrowRecords() {
    fetch('/api/borrowRecords')
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById('borrow-records');
            tableBody.innerHTML = ''; // 清空现有数据

            data.forEach(record => {
                const row = document.createElement('tr');
                
                // 书名、借阅时间、归还时间、逾期状态
                const overdueClass = record.isOverdue ? 'overdue' : 'not-overdue';
                const overdueText = record.isOverdue ? '逾期' : '正常';
                const returnDate = record.returnDate || '--'; // 如果没有归还时间，则显示 "--"
                
                row.innerHTML = `
                    <td>${record.bookTitle}</td>
                    <td>${record.borrowDate}</td>
                    <td>${returnDate}</td>
                    <td><span class="${overdueClass}">${overdueText}</span></td>
                    <td>${record.fine ? record.fine + '元' : '--'}</td>
                    <td>
                        <button class="return-btn" onclick="returnBook('${record.id}')">归还</button>
                        <button class="renew-btn" onclick="renewBook('${record.id}')">续借</button>
                    </td>
                `;
                
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('获取借阅记录失败:', error);
            alert('发生错误，获取借阅记录失败！');
        });
}

// 修改归还图书请求
// 归还图书
function returnBook(recordId) {
    fetch('/api/returnBook', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id: recordId })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('归还成功！');
            fetchBorrowRecords(); // 更新借阅记录列表
        } else {
            alert(data.message || '归还失败！');
        }
    })
    .catch(error => {
        console.error('归还失败:', error);
        alert('发生错误，归还失败');
    });
}

// 修改续借图书请求
// 续借图书
function renewBook(recordId) {
    fetch('/api/renewBook', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id: recordId })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('续借成功！');
            fetchBorrowRecords(); // 更新借阅记录列表
        } else {
            alert(data.message || '续借失败！');
        }
    })
    .catch(error => {
        console.error('续借失败:', error);
        alert('发生错误，续借失败');
    });
}