chrome.runtime.onMessageExternal.addListener((message, sender, sendResponse) => {
    if (message.userId) {
        chrome.storage.local.set({ userId: message.userId }, () => {
            console.log("User ID saved from web app");
            sendResponse({ success: true });
        });
        return true; // Keep channel open for async response
    }
});