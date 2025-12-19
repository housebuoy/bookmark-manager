const API_URL = "https://bookmark-manager-d117.onrender.com/api/bookmarks";
const WEB_APP_URL = "https://bookmark-manager-xyz.vercel.app";

document.addEventListener('DOMContentLoaded', async () => {
  const authView = document.getElementById('authView');
  const saveView = document.getElementById('saveView');
  const titleInput = document.getElementById('titleInput');
  const status = document.getElementById('status');

  // Check for saved session/token
  const { userId } = await chrome.storage.local.get(['userId']);

  if (!userId) {
    authView.classList.remove('hidden');
  } else {
    saveView.classList.remove('hidden');
    const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
    titleInput.value = tab.title;
  }

  document.getElementById('loginBtn').addEventListener('click', () => {
    chrome.tabs.create({ url: `${WEB_APP_URL}?ext_redirect=true` });
  });

  document.getElementById('saveBtn').addEventListener('click', async () => {
    const { userId } = await chrome.storage.local.get(['userId']);
    const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
    const tags = document.getElementById('tagInput').value.split(',').map(t => t.trim());

    const bookmarkRequest = {
      title: titleInput.value,
      url: tab.url,
      favicon: tab.favIconUrl || "",
      description: "Saved via Extension",
      tags: tags,
      isPinned: false,
      isArchived: false
    };

    status.innerText = "Saving...";
    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-User-Id': userId // Matches your current DevUserFilter
        },
        body: JSON.stringify(bookmarkRequest)
      });

      if (response.ok) {
        status.innerText = "Saved successfully!";
        setTimeout(() => window.close(), 1500);
      } else {
        status.innerText = "Error saving bookmark.";
      }
    } catch (e) {
      status.innerText = "Backend not reachable.";
    }
  });
});