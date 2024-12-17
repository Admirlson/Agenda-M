let timeout;

function resetTimer() {
    clearTimeout(timeout);
    // Déconnexion après 14 minutes d'inactivité
    timeout = setTimeout(() => {
        alert("Votre session a expiré en raison d'une inactivité prolongée.");
        window.location.href = '/login'; // URL de déconnexion
    }, 14 * 60 * 1000); // 14 minutes en millisecondes
}

// Réinitialiser le timer lors d'une activité de l'utilisateur
window.onload = resetTimer;
document.onmousemove = resetTimer;
document.onkeypress = resetTimer;
document.onclick = resetTimer;
document.onscroll = resetTimer;
