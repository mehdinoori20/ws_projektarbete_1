// Funktioner för att visa policy-modaler
function showPolicy(policy) {
    document.getElementById(policy + 'Modal').style.display = 'block';
}

function closeModal(policy) {
    document.getElementById(policy + 'Modal').style.display = 'none';
}

// Funktion för att visa "Mer om oss"-modulen
function showAbout() {
    document.getElementById('aboutModal').style.display = 'block';
}

// Funktion för att stänga "Mer om oss"-modulen
function closeAbout() {
    document.getElementById('aboutModal').style.display = 'none';
}

// Stänger modal om användaren klickar utanför innehållet
window.onclick = function (event) {
    const modals = document.querySelectorAll('.modal');
    modals.forEach(modal => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
};