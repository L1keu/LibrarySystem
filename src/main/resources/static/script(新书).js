// 新增图书处理函数
function addBook(event) {
    event.preventDefault(); // 防止表单默认提交行为

    // 获取表单数据
    const title = document.getElementById('book-title').value;
    const author = document.getElementById('book-author').value;
    const isbn = document.getElementById('book-isbn').value;
    const publisher = document.getElementById('book-publisher').value;
    const publishDate = document.getElementById('book-publish-date').value;
    const description = document.getElementById('book-description').value;
    const price = document.getElementById('book-price').value;

    // 发送请求到后端
    fetch('/api/addBook', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            title: title,
            author: author,
            isbn: isbn,
            publisher: publisher,
            publishDate: publishDate,
            description: description,
            price: price
        }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('图书添加成功');
            // 重定向到图书管理页面
            window.location.href = '图书管理.html';
        } else {
            alert('添加图书失败: ' + data.message);
        }
    })
    .catch(error => {
        console.error('新增图书请求失败:', error);
        alert('新增图书失败，请稍后再试！');
    });
}