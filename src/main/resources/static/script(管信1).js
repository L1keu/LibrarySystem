
// 模拟跳转到不同的管理功能页面
function navigateTo(page) {
    switch(page) {
        case 'book-management':
            alert("跳转到图书管理页面");
            break;
        case 'system-management':
            alert("跳转到系统管理页面");
            break;
        default:
            alert("未定义的页面");
    }
}