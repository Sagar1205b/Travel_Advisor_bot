let chats = JSON.parse(localStorage.getItem("chatSessions")) || [];
let currentChatIndex = chats.length ? chats.length - 1 : -1;

const chatListEl = document.getElementById("chatList");
const chatHistoryEl = document.getElementById("chatHistory");
const chatForm = document.getElementById("chatForm");
const messageInput = document.getElementById("messageInput");
const newChatBtn = document.getElementById("newChatBtn");

function renderChatList() {
    chatListEl.innerHTML = "";
    chats.forEach((chat, index) => {
        const li = document.createElement("li");
        li.textContent = chat.title || `Chat ${index + 1}`;
        li.onclick = () => loadChat(index);
        chatListEl.appendChild(li);
    });
}

function renderMessages() {
    if (currentChatIndex < 0) return;
    const chat = chats[currentChatIndex];
    chatHistoryEl.innerHTML = "";
    chat.messages.forEach(msg => {
        const div = document.createElement("div");
        div.className = `chat-message ${msg.sender}`;
        div.textContent = msg.text;
        chatHistoryEl.appendChild(div);
    });
}

function saveChats() {
    localStorage.setItem("chatSessions", JSON.stringify(chats));
}

function newChat() {
    const newChat = {
        title: `Chat ${chats.length + 1}`,
        messages: []
    };
    chats.push(newChat);
    currentChatIndex = chats.length - 1;
    saveChats();
    renderChatList();
    renderMessages();
}

function loadChat(index) {
    currentChatIndex = index;
    renderMessages();
}

chatForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const text = messageInput.value.trim();
    if (!text || currentChatIndex < 0) return;

    chats[currentChatIndex].messages.push({ sender: "user", text });
    // Here you could simulate a response, e.g.:
    chats[currentChatIndex].messages.push({ sender: "bot", text: "Echo: " + text });

    messageInput.value = "";
    saveChats();
    renderMessages();
});

newChatBtn.addEventListener("click", newChat);

// Initial render
if (chats.length === 0) {
    newChat();
} else {
    renderChatList();
    loadChat(currentChatIndex);
}
