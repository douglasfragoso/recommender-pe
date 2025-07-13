import matplotlib.pyplot as plt
import pandas as pd

recommendation_data = [
    {
        "poiId": 4,
        "cosine": 0.6018596838166653,
        "euclidean": 0.528444704337101,
        "pearson": 0.7534036996200997,
        "jaccard": 0.3415724876803922,
        "average": 0.5563201438635645
    },
    {
        "poiId": 36,
        "cosine": 0.5587987886739322,
        "euclidean": 0.5156332858408927,
        "pearson": 0.7200791870808965,
        "jaccard": 0.30316072034103947,
        "average": 0.5244179954841902
    },
    {
        "poiId": 7,
        "cosine": 0.3600625334861511,
        "euclidean": 0.4691937745640616,
        "pearson": 0.589086757538062,
        "jaccard": 0.17493202225748664,
        "average": 0.3983187719614403
    },
    {
        "poiId": 29,
        "cosine": 0.3681303531388414,
        "euclidean": 0.4707739759181946,
        "pearson": 0.6067801679113831,
        "jaccard": 0.1454479726888634,
        "average": 0.3977831174143206
    },
    {
        "poiId": 30,
        "cosine": 0.3681303531388414,
        "euclidean": 0.4707739759181946,
        "pearson": 0.6067801679113831,
        "jaccard": 0.1454479726888634,
        "average": 0.3977831174143206
    }
]

df = pd.DataFrame(recommendation_data)

df.set_index('poiId', inplace=True)

fig, ax = plt.subplots(figsize=(10, 6)) # Tamanho da figura

markers = ['o', 's', '^', 'D', 'X'] 
metric_names = ['Cosseno', 'Euclidiana', 'Pearson', 'Jaccard', 'Média']

for i, metric in enumerate(['cosine', 'euclidean', 'pearson', 'jaccard', 'average']):
    ax.plot(df.index.astype(str), df[metric],
            label=metric_names[i], 
            color='black',
            linestyle='-',
            marker=markers[i], 
            markersize=8, 
            linewidth=1.5)

ax.set_xlabel('ID do POI', color='black')
ax.set_ylabel('Valor de Similaridade', color='black')
ax.set_title('Exemplo de Recomendação por Métrica de Similaridade', color='black')

ax.tick_params(axis='x', colors='black')
ax.tick_params(axis='y', colors='black')

ax.set_ylim(0, 1.0)

ax.grid(axis='y', linestyle='--', alpha=0.7, color='grey')

ax.spines['top'].set_visible(False)
ax.spines['right'].set_visible(False)
ax.spines['bottom'].set_color('black')
ax.spines['left'].set_color('black')

ax.legend(title='Métrica', frameon=False, loc='center left', bbox_to_anchor=(1, 0.5))
plt.tight_layout(rect=[0, 0, 0.85, 1]) 

plt.show()
