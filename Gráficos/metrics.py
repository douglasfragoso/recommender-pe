import matplotlib.pyplot as plt

metrics_bw = {
    "Average Precision@K": 0.79,
    "Hit Rate@K": 1.00,
    "Item Coverage": 0.86,
    "Intra-list Similarity": 0.41
}

labels = list(metrics_bw.keys())
values = list(metrics_bw.values())

fig, ax = plt.subplots(figsize=(8, 6))
bars = ax.bar(labels, values, color='black', edgecolor='white')

ax.set_title("Métricas de Avaliação - Sistema de Recomendação (30 usuários)", fontsize=13)
ax.set_ylabel("Valor Normalizado (0 a 1)")
ax.set_ylim(0, 1.1)
ax.grid(axis='y', linestyle='--', linewidth=0.5, alpha=0.7, color='gray')

for bar in bars:
    height = bar.get_height()
    ax.text(bar.get_x() + bar.get_width() / 2, height + 0.02, f'{height:.2f}',
            ha='center', va='bottom', color='black', fontsize=10)

plt.tight_layout()
plt.show()