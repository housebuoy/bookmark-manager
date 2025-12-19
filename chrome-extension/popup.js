const API_BASE_URL = "https://bookmark-manager-d117.onrender.com/api";
const WEB_APP_URL = "https://bookmark-manager-xyz.vercel.app";

document.addEventListener('DOMContentLoaded', async () => {
    const authView = document.getElementById('authView');
    const saveView = document.getElementById('saveView');
    const titleInput = document.getElementById('titleInput');
    const tagInput = document.getElementById('tagInput');
    const descInput = document.getElementById('descInput'); // Ensure this ID exists in your HTML
    const status = document.getElementById('status');
    const saveBtn = document.getElementById('saveBtn');

    // Check for saved session/token
    const { userId } = await chrome.storage.local.get(['userId']);

    if (!userId) {
        authView.classList.remove('hidden');
    } else {
        saveView.classList.remove('hidden');
        const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
        
        // Initial setup with tab data
        titleInput.value = tab.title;
        
        // ✨ Auto-Fill Scraper Logic
        // Triggers immediately to fetch details like the site does
        status.innerText = "Auto-filling details...";
        try {
            const scraperResponse = await fetch(`${API_BASE_URL}/scraper/preview`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "X-User-Id": userId,
                },
                body: JSON.stringify({ url: tab.url }),
            });

            if (scraperResponse.ok) {
                const data = await scraperResponse.json();
                // Fill fields with scraped data if available
                titleInput.value = data.title || tab.title;
                if (descInput) descInput.value = data.description || "";
                if (tagInput) tagInput.value = data.tags ? data.tags.join(", ") : "";
                status.innerText = "Details auto-filled!";
            } else {
                status.innerText = "Using tab title (Scraper unavailable).";
            }
        } catch (error) {
            console.error("Scraping error:", error);
            status.innerText = "Using tab title (Scraper error).";
        }
    }

    document.getElementById('loginBtn').addEventListener('click', () => {
        chrome.tabs.create({ url: `${WEB_APP_URL}/sign-in?ext_redirect=true` });
    });

    saveBtn.addEventListener('click', async () => {
        const { userId } = await chrome.storage.local.get(['userId']);
        const [tab] = await chrome.tabs.query({ active: true, currentWindow: true });
        
        // Split tags string back into an array for the backend
        const tags = tagInput.value 
            ? tagInput.value.split(',').map(t => t.trim()).filter(Boolean) 
            : [];

        const bookmarkRequest = {
            title: titleInput.value,
            url: tab.url,
            favicon: tab.favIconUrl || `https://www.google.com/s2/favicons?sz=64&domain_url=${tab.url}`,
            description: descInput ? descInput.value : "Saved via Extension",
            tags: tags,
            isPinned: false,
            isArchived: false,
            viewCount: 0
        };

        saveBtn.disabled = true;
        status.innerText = "Saving...";
        try {
            const response = await fetch(`${API_BASE_URL}/bookmarks`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-User-Id': userId
                },
                body: JSON.stringify(bookmarkRequest)
            });

            if (response.ok) {
                status.innerText = "Saved successfully!";
                setTimeout(() => window.close(), 1500);
            } else {
                status.innerText = "Error saving bookmark.";
                saveBtn.disabled = false;
            }
        } catch (e) {
            status.innerText = "Backend not reachable.";
            saveBtn.disabled = false;
        }
    });
});