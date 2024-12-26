// 页面加载时获取图书详细信息
window.onload = async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const bookId = urlParams.get('id');
    
    if (!bookId) {
        alert('图书ID参数缺失');
        return;
    }

    try {
        // 获取图书详细信息
        const response = await fetch(`/api/books/${bookId}`);
        const data = await response.json();

        if (data.success) {
            const book = data.data;
            document.getElementById('book-title').textContent = book.title;
            document.getElementById('book-author').textContent = book.author;
            document.getElementById('book-isbn').textContent = book.isbn;
            document.getElementById('book-publisher').textContent = book.publisher;
            document.getElementById('book-pub-date').textContent = book.publishDate;
            document.getElementById('book-description').textContent = book.description;
            document.getElementById('book-stock').textContent = book.stock;
            document.getElementById('book-status').textContent = book.stock > 0 ? '可借阅' : '已借完';
            document.getElementById('book-cover').src = book.coverImageUrl;

            const borrowBtn = document.getElementById('borrow-btn');
            borrowBtn.disabled = book.stock <= 0;
            borrowBtn.textContent = book.stock <= 0 ? '已借完' : '借阅';
        } else {
            alert('图书信息加载失败');
        }
    } catch (error) {
        alert('加载图书信息时发生错误');
    }
};

// 借阅书籍
async function borrowBook() {
    const urlParams = new URLSearchParams(window.location.search);
    const bookId = urlParams.get('id');
    
    if (!bookId) {
        alert('图书ID参数缺失');
        return;
    }

    try {
        const response = await fetch(`/api/borrow/${bookId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`, // 使用存储在本地的JWT token
            },
        });
        const data = await response.json();

        if (data.success) {
            alert('借阅成功！');
            const book = data.data;
            document.getElementById('book-stock').textContent = book.stock;
            document.getElementById('book-status').textContent = book.stock > 0 ? '可借阅' : '已借完';

            const borrowBtn = document.getElementById('borrow-btn');
            borrowBtn.disabled = book.stock <= 0;
            borrowBtn.textContent = book.stock <= 0 ? '已借完' : '借阅';
        } else {
            alert('借阅失败，请稍后再试');
        }
    } catch (error) {
        alert('借阅图书时发生错误');
    }
}