document.addEventListener('DOMContentLoaded', () => {
  const saveBtn = document.getElementById('saveBtn');
  const userIdInput = document.getElementById('userId');
  const status = document.getElementById('status');

  // Load saved User ID from storage
  chrome.storage.local.get(['savedUserId'], (result) => {
    if (result.savedUserId) userIdInput.value = result.savedUserId;
  });

  saveBtn.addEventListener('click', async () => {
    const userId = userIdInput.value.trim();
    if (!userId) {
      status.innerText = "Error: User ID is required.";
      return;
    }

    // Save User ID for future use
    chrome.storage.local.set({ savedUserId: userId });

    // Get active tab details
    const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });

    const bookmarkRequest = {
      title: tab.title,
      url: tab.url,
      favicon: tab.favIconUrl || "",
      description: "Saved via Chrome Extension",
      tags: ["extension"],
      isPinned: false,
      isArchived: false
    };

    status.innerText = "Saving...";

    try {
      const response = await fetch('http://localhost:8080/api/bookmarks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-User-Id': userId // Required by your DevUserFilter
        },
        body: JSON.stringify(bookmarkRequest)
      });

      if (response.ok) {
        status.innerText = "Successfully saved!";
        setTimeout(() => window.close(), 1500);
      } else {
        status.innerText = "Failed to save bookmark.";
      }
    } catch (error) {
      status.innerText = "Error: Could not connect to backend.";
      console.error(error);
    }
  });
});