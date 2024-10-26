import matplotlib.pyplot as plt
from datetime import datetime, timedelta
from github import Github
import os

# Variables GitHub (peut être ajoutées dans GitHub Secrets)
TOKEN = os.getenv("GITHUB_TOKEN")
REPO_NAME = "cju3ry/projet_Musee"  # Remplacez par le nom de votre dépôt
MILESTONE_TITLE = "Sprint 1"

# Connexion à l'API GitHub
g = Github(TOKEN)
repo = g.get_repo(REPO_NAME)

# Récupération de la milestone
milestones = repo.get_milestones()
milestone = next((m for m in milestones if m.title == MILESTONE_TITLE), None)
if milestone is None:
    print(f"Milestone '{MILESTONE_TITLE}' non trouvée.")
    exit(1)

# Calcul des issues ouvertes et fermées dans la milestone
total_issues = milestone.open_issues + milestone.closed_issues
issues_restantes = [total_issues - i for i in range(milestone.closed_issues + 1)]

# Créer le burndown chart
jours = list(range(len(issues_restantes)))
plt.figure(figsize=(10, 6))
plt.plot(jours, issues_restantes, label="Burndown", color="blue", marker="o")
plt.title("Burndown Chart pour la Milestone")
plt.xlabel("Jours depuis le début")
plt.ylabel("Issues restantes")
plt.legend()
plt.grid(True)
plt.savefig("burndown_chart.png")
plt.show()
