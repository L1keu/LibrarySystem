// 获取图书数据
function fetchBooks(query = '') {
    // 请求后端 API 获取图书数据
    fetch(`/api/books?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(data => {
            const booksTableBody = document.querySelector('#books-table tbody');
            booksTableBody.innerHTML = ''; // 清空当前表格内容

            if (Array.isArray(data) && data.length > 0) {
                // 动态填充图书数据
                data.forEach(book => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.category}</td>
                        <td>${book.stock}</td>
                        <td>${book.borrowStatus}</td>
                        <td>
                            <button class="edit-btn" onclick="editBook('${book.id}')">编辑</button>
                            <button class="delete-btn" onclick="deleteBook('${book.id}')">删除</button>
                        </td>
                    `;
                    booksTableBody.appendChild(row);
                });
            } else {
                booksTableBody.innerHTML = '<tr><td colspan="6">没有找到相关图书</td></tr>';
            }
        })
        .catch(error => {
            console.error('获取图书数据失败:', error);
            alert('获取图书数据失败');
        });
}

// 搜索功能
function searchBooks() {
    const query = document.getElementById('search-input').value;
    fetchBooks(query);  // 调用获取图书数据的函数，传入搜索关键词
}

// 编辑图书（跳转到编辑页面）
function editBook(bookId) {
    window.location.href = `edit-book.html?id=${bookId}`;
}

// 删除图书
function deleteBook(bookId) {
    if (confirm('确认删除此图书？')) {
        // 发送 DELETE 请求到后端
        fetch(`/api/books/${bookId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('图书删除成功');
                fetchBooks();  // 重新加载图书列表
            } else {
                alert('图书删除失败');
            }
        })
        .catch(error => {
            console.error('删除图书失败:', error);
            alert('删除图书失败');
        });
    }
}

// 页面加载时获取图书数据
document.addEventListener('DOMContentLoaded', function() {
    fetchBooks(); // 初次加载时获取所有图书数据
});