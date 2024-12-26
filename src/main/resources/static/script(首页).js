document.addEventListener("DOMContentLoaded", function () {
    // 模拟用户登录状态，假设当前用户没有登录
    let loggedIn = false;  // 你可以将其改为 true 来模拟已登录状态
    const loginButton = document.getElementById("login-btn");
    const registerButton = document.getElementById("register-btn");
    const userDetails = document.getElementById("user-details");
    const username = document.getElementById("username");

    // 根据登录状态来显示用户信息或登录/注册按钮
    function updateUserStatus() {
        if (loggedIn) {
            loginButton.style.display = 'none';
            registerButton.style.display = 'none';
            userDetails.style.display = 'inline-block';
            username.textContent = "张三";  // 假设用户名是张三
        } else {
            loginButton.style.display = 'inline-block';
            registerButton.style.display = 'inline-block';
            userDetails.style.display = 'none';
        }
    }

    // 初始时更新用户状态
    updateUserStatus();

    // 登录按钮点击事件
    loginButton.addEventListener("click", function () {
        // 假设跳转到登录页面
        window.location.href = "login.html";  // 你可以根据需要修改为实际的登录页面路径
    });

    // 注册按钮点击事件
    registerButton.addEventListener("click", function () {
        // 假设跳转到注册页面
        window.location.href = "register.html";  // 你可以根据需要修改为实际的注册页面路径
    });

    // 用户信息点击事件（假设点击用户名可以显示更多选项）
    document.getElementById("user-details").addEventListener("click", function () {
        // 假设点击后跳转到用户个人信息页
        window.location.href = "user_profile.html";  // 你可以根据需要修改为实际的用户信息页面路径
    });

    //header隐藏
    let lastScrollTop = 0; // 上次滚动位置
let header = document.querySelector("header"); // 获取 header 元素

window.addEventListener("scroll", function() {
    let currentScroll = window.pageYOffset || document.documentElement.scrollTop;

    // 判断滚动方向
    if (currentScroll > lastScrollTop) {
        // 向下滚动，隐藏 header
        header.style.top = "-100px"; // 隐藏，调整值根据 header 的高度来设置
    } else {
        // 向上滚动，显示 header
        header.style.top = "0";
    }
    lastScrollTop = currentScroll <= 0 ? 0 : currentScroll; // 防止负值
});

});