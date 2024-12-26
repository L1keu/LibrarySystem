// 获取URL中的查询参数
function getQueryParam(name) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(name);
}

// 获取搜索参数并展示搜索结果
function displaySearchResults(query) {
    // 向后端发送请求，获取书籍数据
    fetch(`/api/search?query=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(books => {
            const resultsList = document.getElementById("results-list");
            resultsList.innerHTML = '';  // 清空现有结果

            if (books.length > 0) {
                books.forEach(book => {
                    const li = document.createElement("li");
                    li.textContent = `${book.title} - 作者: ${book.author}`;
                    resultsList.appendChild(li);
                });
            } else {
                resultsList.innerHTML = "<li>没有找到相关书籍</li>";
            }
        })
        .catch(error => {
            console.error("Error fetching search results:", error);
        });
}

// 当页面加载时显示搜索结果
window.onload = function() {
    const query = getQueryParam("query");
    if (query) {
        displaySearchResults(query);
    }
};