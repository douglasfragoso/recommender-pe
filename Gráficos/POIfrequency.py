import matplotlib.pyplot as plt

poi_frequency = {
    "1": 0.1, "2": 0.1, "3": 0.1, "4": 0.2, "5": 0.03, "6": 0.2, "7": 0.13,
    "8": 0.27, "9": 0.03, "10": 0.13, "11": 0.03, "12": 0.1, "13": 0.0,
    "14": 0.1, "15": 0.0, "16": 0.07, "17": 0.2, "18": 0.37, "19": 0.1,
    "20": 0.27, "21": 0.23, "22": 0.23, "23": 0.0, "24": 0.0, "25": 0.03,
    "26": 0.17, "27": 0.17, "28": 0.37, "29": 0.13, "30": 0.13, "31": 0.1,
    "32": 0.33, "33": 0.0, "34": 0.2, "35": 0.1, "36": 0.27
}

poi_ids = list(poi_frequency.keys())
frequencies = list(poi_frequency.values())

plt.figure(figsize=(10, 5)) 
plt.plot(poi_ids, frequencies, 'o-', color='black', linewidth=1, markersize=4) 

plt.title('Frequência de Pontos de Interesse (POI)', color='black')
plt.xlabel('ID do POI', color='black')
plt.ylabel('Frequência', color='black')

plt.xticks(rotation=90, color='black', fontsize=8) 
plt.yticks(color='black')
plt.tick_params(axis='x', colors='black')
plt.tick_params(axis='y', colors='black')

plt.box(on=None)

plt.grid(axis='y', linestyle='--', alpha=0.7, color='grey')

plt.tight_layout() 
plt.show()

