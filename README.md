## License

This project is proprietary. The source code is made available exclusively for academic study and evaluation purposes.

[![License: Proprietary](https://img.shields.io/badge/license-proprietary-red.svg)](https://github.com/douglasfragoso/recommender-pe?tab=License-1-ov-file)

## Authors and Affiliations

**Douglas Inácio Fragoso Ferreira¹\***; **Everton Gomede²**

1. University of São Paulo (USP). MBA in Software Engineering, Luiz de Queiroz College of Agriculture (Esalq), Pecege.
2. School of Electrical and Computer Engineering, State University of Campinas (FEEC/UNICAMP). PhD in Computer Science.

\*Corresponding author: douglas.iff@gmail.com

# Points of Interest Recommendation System in Recife with Content-Based Filtering and Pernambuco Passport

## Project Overview

Recife has significant tourism potential but requires digital solutions to promote its Points of Interest (POIs). Thus, the implementation of an intelligent system represents an innovation opportunity for the sector. A Content-Based Recommendation System was proposed, which uses similarity metrics to correlate user preferences with POI characteristics. 
The system achieved 79% precision, 100% minimum hit rate, and 86% coverage, demonstrating strong performance in cold start scenarios despite the overexposure of some POIs. Therefore, the model proved effective in providing personalized recommendations, showing potential to boost local tourism.

### MVP Goals

- User registration and preference collection
- POI catalog ingestion based on Passaporte Pernambuco
- Similarity computation and route generation
- Evaluation using Information Retrieval metrics (Precision@k, Hit Rate@k, Item Coverage, etc.)

## Technologies Used

| **Technology**                     | **Function**                                                                 |
|------------------------------------|-----------------------------------------------------------------------------|
| Java 21, Spring Boot, and Maven       | For API development                                                         |
| Spring Security and JSON Web Token | For security implementation                                                 |
| Spring Web MVC                     | For REST and MVC architecture                                               |
| Spring Data JPA, H2 Database, MySQL, and MySQL Connector | For data storage and manipulation                      |
| Spring Doc and Swagger             | For API documentation                                                       |
| Java Bean Validation and Lombok    | For code simplification and efficiency                                       |
| Spring Test, JUnit5, Mockito | For unit testing                                                          |
| JaCoCo, Maven Surefire Plugin, and Maven Surefire Report Plugin | For code coverage and test reporting           |
| Postman                            | For manual endpoint testing                                                 |
| Git and GitHub                     | For code versioning                                                         |
| Apache Common Math3                | To calculate similarity metrics and evaluation measures                  |
| Visual Studio Code                 | For code editing                                                            |

## Methodology

### Content-Based Filtering

Content-based filtering uses descriptive information to recommend items to users. This approach combines user information and item information to generate personalized preferences (Aggarwal, 2016; Borràs et al., 2014; Gao et al., 2010; Li et al., 2021).

Typically, ranking in content-based filtering is based on the distance between candidate items and user preferences, focusing on two types of information: the user's preference model and/or the user's interaction history (Li et al., 2021). There are several ways to analyze descriptive data in content-based filtering, where traditionally, term weighting and similarity computation are used, such as **Term Frequency - Inverse Document Frequency (TF-IDF)** and similarity metrics like **Euclidean Distance (ED)**, **Cosine Similarity (COS)**, **Pearson Correlation Coefficient (PCC)**, and **Jaccard Coefficient (JC)** (Castells & Jannach, 2023; Sondur & Chigadani, 2016).

This algorithm helps compare user preferences with the description of Points of Interest (POIs). For example, if a user prefers "beach," TF-IDF will identify POIs where the term is relevant. TF-IDF creates vectors, and the similarity between these vectors is calculated to measure how close they are, ranging from 0 to +1 (0 indicating complete opposition and +1 indicating identical vectors), with **Cosine Similarity (COS)** being the standard method (Gao et al., 2010; Salton & Buckley, 1988).

In this study, to calculate similarity metrics, a combination of TF-IDF, COS, ED, JC and PCC is used, along with evaluation metrics composed of Precision@k, Hit Rate@k, and Item Coverage. These metrics are described in detail in the subsections below.

### Similarity Metrics

The calculation of similarity metrics begins with **TF-IDF**, a numerical statistical technique that determines the relevance of a term in a set of documents (Qaiser and Ali, 2018; Salton and Buckley, 1988; Ramos, 2003). Salton and Buckley (1988) consider the TF-IDF algorithm simple and efficient. TF is calculated as shown in equations (1), (2), and (3):

    TF(t, d) = freq(t) / (total number of words in the document) (1)

    IDF(t) = log(|D| / DF(t)) = log((total number of documents) / (number of documents containing term t)) (2)

    TF-IDF(t, d) = TF(t, d) * IDF(t) (3)

Using the vector values, it is possible to obtain the similarity between them by normalizing the term weights using the cosine of the angle, demonstrating how relevant the term is in a set of documents (Salton and Buckley, 1988; Sondur and Chigadani, 2016).

To calculate **COS**, the product of the vectors is obtained, followed by normalizing the query and document vectors, and finally dividing the results (Gao et al., 2010; Salton and Buckley, 1988), as shown in equations (4), (5), and (6):

    ∑(k=1 to n) w_{qk} * w_{dk} (4)
    
    |Q| = √(∑(k=1 to n) (w_{qk})^2), |D| = √(∑(k=1 to n) (w_{dk})^2) (5)
    
    similarity(Q, D) = cos(w_{qk}, w_{dk}) = (∑(k=1 to n) w_{qk} * w_{dk}) / (√(∑(k=1 to n) (w_{qk})^2 * √(∑(k=1 to n) (w_{dk})^2))) (6)

Where w_{qk} and w_{dk} are the TF-IDF weights of term k in vectors Q and D, respectively.

**ED** calculates the distance between two points through the square root of the sum of the squares of the differences of the vectors \(Q = (q_1, q_2, \dots, q_n)\) and \(D = (d_1, d_2, \dots, d_n)\), which represent the relevance of terms in documents based on TF-IDF (Danielsson, 1980; Sondur and Chigadani, 2016), as shown in equation (7):

    ED(Q, D) = √(∑(k=1 to n) (q_k - d_k)^2) (7)

ED is inversely proportional (Sondur and Chigadani, 2016), so the smaller the result, the more similar the terms are. To obtain an indicator similar to the COS metric, ED must be inverted, as in equation (8):

    similarity(Q, D) = Inverse ED(Q, D) = 1 / (1 + Euclidean distance(q, d)) (8)

Thus, the Euclidean similarity indicates that the higher the result, the greater the similarity between the vectors.

**PCC** is a metric used to measure the strength of the linear relationship between two vectors (Sondur and Chigadani, 2016) and helps capture how the relative frequencies of terms in documents are correlated. To calculate PCC, the mean of vectors \(Q\) and \(D\) is obtained, followed by measuring the covariance and standard deviation between the vectors, as in equations (9), (10), (11), and (12):

    Q_med = (1/n) * ∑(k=1 to n) q_k, D_med = (1/n) * ∑(k=1 to n) d_k (9)

    ∑(k=1 to n) (q_k - Q_med)(d_k - D_med) (10)

    √(∑(k=1 to n) (q_k - Q_med)^2) * √(∑(k=1 to n) (d_k - D_med)^2) (11)

    Pearson Correlation Coefficient = r(Q, D) = (∑(k=1 to n) (q_k - Q_med)(d_k - D_med)) / (√(∑(k=1 to n) (q_k - Q_med)^2 * √(∑(k=1 to n) (d_k - D_med)^2)) (12)

The result of this calculation ranges from -1 to 1, where -1 indicates a perfect negative linear correlation and 1 indicates a perfect positive linear correlation (Sondur and Chigadani, 2016). Therefore, like ED, the result must be normalized, as seen in equation (13):

    Pearson similarity(Q, D) = (r(Q, D) + 1) / 2 (13)

Thus, Pearson similarity ranges from 0 to 1, where 0 indicates complete opposition and 1 indicates identical documents.

Finally, the **Jaccard Coefficient (JC)** measures similarity as the ratio between the intersection and the union of the values of two vectors. When applied to text documents, the JC compares the sum of the weights of the shared terms with the sum of the weights of the terms present in at least one of the vectors, as shown in Equation (14):

    Jaccard Coefficient = min∑(k=1 to n) (xk, yk) / max∑(k=1 to n) (xk, yk) (14)

The JC value ranges from 0 to 1, similarly to the Cosine Similarity (COS), with higher values indicating greater similarity.

### Evaluation Metrics

The metrics used in this system are Item-Based Indicators in terms of order, aiming to evaluate the performance of the recommended item lists. The first metric used is **Precision@k**, which focuses on the quality of recommendations through the proportion of relevant items in the top-k recommended items (Li et al., 2021), as described in equation (15):

    Precision@k = (Relevant Items in Top-k) / k (15)

Moreover, to estimate the variability of the mean **Precision@k** metric obtained from the recommendations, a **Confidence Interval (CI)** should be calculated based on the t-Student distribution, as recommended in situations where the population variance is unknown and the sample size is relatively small (n < 100) (Fernandes, 1999). The construction of this interval is based on the T-statistic formula, presented in Eq. (16):

    T = (x̄ (Sample mean) - μ (True population mean)) / (s (sample standard deviation) / √n (sample size)) (16)

For a 95% confidence level, the critical value is obtained from the t-Student distribution with n–1 degrees of freedom. The Margin of Error (ME) is then calculated using Eq. (17). Thus, the CI for the mean is defined by Eq. (18).

    ME = t_(α/2,n-1) (critical value) × (s (sample standard deviation) / √n (sample size)) (17)

    CI = x̄ ± ME (18)
 
The **Hit Rate@k** metric focuses on ensuring that each user receives something relevant in the top-k, verifying the proportion of users served (Deshpande and Karypis, 2004), as described in equation (19):

    Hit Rate@k = (Users with at least 1 relevant item in Top-k) / (Total Users) (19)

The **Item Coverage** metric focuses on the diversity of recommendations, measuring the proportion of recommendable items that were effectively recommended to all users (Castells and Jannach, 2023), as in equation (20):

    Item Coverage = (Number of Unique Recommended Items) / (Total Available Items) (20)

In turn, **Intra-List Similarity** is based on the calculation of the average similarity between item pairs, allowing the evaluation of item diversity in the recommendations (Jesse et al., 2023). To obtain this metric, the system reuses the code used to generate the recommendations. Finally, **Feature Coverage** and **POI Frequency** provide insights into the usage coverage of the features utilized by users and the frequency with which Points of Interest (POIs) appear in the recommendations.

## Regional Context: Passaporte Pernambuco

The consumption of Points of Interest (POIs) in the Recommender System corresponds to that of **Passaporte Pernambuco**, a symbolic document created by the **Secretariat of Tourism and Leisure of the State of Pernambuco (Setur)** in 2020, with the aim of promoting tourism in the state. With this passport in hand, visitors can register their visits to tourist attractions in Pernambuco cities through a stamp (Fonseca, 2020; Valença, 2022). In the case of the Recommender System in this MVP, only POIs in the capital of Pernambuco, **Recife**, are considered.

The capital has a diversified economy, with a focus on exports, business, technology, culture, and more recently, tourism, which has become an important strategy for local development (Morais et al., 2022). During Carnival alone, the city attracted over 3.4 million people in 2024, generating approximately 2.4 billion reais (Costa, 2024). Recife is also known for its museums, parks, bridges, squares, and buildings considered historical heritage sites (Morais et al., 2022).

The capital of Pernambuco approaches tourism with creativity and sustainability, envisioning it as "a new way of doing tourism," particularly through strategies that immerse tourists in the city's culture (Campos et al., 2022). With the passport, visitors can stamp at least 36 different tourist attractions in Recife, as shown in the table below:

### Tourist Attractions in Recife

| Number | Description |
|--------|-------------|
| 1      | Basílica Nossa Senhora do Carmo |
| 2      | Bistrô Negra Linda |
| 3      | Cachaçaria Carvalheira |
| 4      | Cais do Sertão |
| 5      | Caixa Cultural |
| 6      | Capela Dourada |
| 7      | Casa da Cultura |
| 8      | Catamaran Tours |
| 9      | Centro de Artesanato de Pernambuco |
| 10     | Concatedral de São Pedro dos Clérigos |
| 11     | Embaixada de Pernambuco |
| 12     | Embaixada dos Bonecos Gigantes |
| 13     | Forte das Cinco Pontas – Museu da Cidade |
| 14     | Forte do Brum |
| 15     | Fundação Gilberto Freyre |
| 16     | Igreja de Santa Tereza D'Avila |
| 17     | Igreja Madre de Deus |
| 18     | Igreja Nossa Senhora do Rosário dos Homens Pretos |
| 19     | Instituto Ricardo Brennand |
| 20     | Jardim Botânico |
| 21     | Museu da Abolição |
| 22     | Museu de Arte Moderna Aloísio Magalhães |
| 23     | Museu do Estado de Pernambuco |
| 24     | Museu do Homem do Nordeste |
| 25     | Museu do Trem |
| 26     | Museu Murilo La Greca |
| 27     | Oficina Francisco Brennand |
| 28     | Paço do Frevo |
| 29     | Palácio da Justiça |
| 30     | Palácio do Campo das Princesas |
| 31     | Pracinha de Boa Viagem |
| 32     | Sede do Galo da Madrugada |
| 33     | Sinagoga Kahal Zur Israel |
| 34     | Teatro de Santa Isabel |
| 35     | Teatro do Parque |
| 36     | Torre Malakoff |

**Source:** Adapted from Secretaria de Turismo e Lazer (2020).

## Results of the project

The MVP of this project implemented a Point of Interest (POI) Recommender System (RS) in Recife, focusing on General POI Recommendation. The recommendations are based on the content of POI attributes and the user's profile, considering their preference model.

To achieve this, a comparison is made using similarity measures between the user's preference terms and the descriptions of the POIs. Finally, the evaluation of these recommendations is performed in a binary and explicit format (like and dislike), which is later analyzed using evaluation metrics.

The system flow begins with **User and Preference Registration** and **POI Registration**. The user registers their general information and preferences, such as specific interests (motivations, hobbies, and themes). This information is stored in the database with static attributes (personal data) and dynamic attributes (preferences and interaction history).

The POIs (such as museums, churches, and restaurants) are registered with attributes like name, description, and preference types. When the user requests a recommendation, the system compares the user's preferences with the POI descriptions using **TF-IDF** to calculate the relevance of the terms.

With the TF-IDF values, it is possible to calculate the **Similarity Metrics** (composed of **COS**, **ED**, **PCC**, and **JC**) and then obtain the average of these metrics. The resulting values are used to measure the proximity between the user's profile and the POIs. Subsequently, the most relevant POIs are ranked and returned as a top-k list to the user.

The user evaluates the recommended POIs as **like** or **dislike**. These evaluations are stored in the database and can be analyzed using the metrics **Precision@k**, **Hit Rate@k**, **Item Coverage**, **Intra-List Similarity**, **Feature Coverage**, and **POI Frequency**.

In this stage, 36 POIs from the Passaporte Pernambuco in Recife were used. Additionally, 30 fictional users were generated with their preferences and ratings using Large Language Models (LLMs). This synthetic data aimed to simulate realistic tourist behaviors and proved suitable for the initial technical validation of the proposed algorithm.

Each user received a personalized list of recommended POIs based on their profile and interactions, recorded in the database through binary like/dislike ratings. These records were used to calculate the evaluation metrics discussed earlier, as illustrated in Figure 3. With the simulated data, basic statistics were extracted, and trends in the algorithm's behavior were identified. The recommender system (RS) achieved an average precision of 79% in the relevance of recommended POIs, with a 95% Confidence Interval (CI) between 73% and 85%. Furthermore, 100% of users received at least one relevant POI.

In terms of item coverage, 86% of the POIs were recommended at least once, and the average intra-list similarity among recommended items was 41%. Although the data used was simulated, the results were considered robust: the RS showed a good balance between engagement and diversity, avoiding excessive redundancy in suggested items while maintaining high recommendation accuracy.

As shown in Figure 4, the system failed to recommend 13.88% of the POIs, with items 13, 15, 23, 24, and 33 appearing 0% of the time in the recommended lists. In contrast, some POIs stood out due to excessive popularity: items 8, 18, 20, 28, 32, and 36 were recommended between 27% and 37% of the time, reaching a popularity of 16.66%.

The overexposure and underutilization of certain POIs may lead to recommendation saturation, harming the personalized experience of tourists as their preferences change. These distortions are possibly linked to uneven category coverage and algorithmic bias stemming from textual similarity.

In summary, the LLM-generated synthetic data allowed us to observe the typical behavior of a content-based RS, highlighting its strengths and limitations: satisfactory performance in cold start scenarios but a tendency to repeat certain items in recommendations. This scenario underscored the need for strategies to expand item coverage and promote greater balance in recommendations.

# UML CLASSES

## Classes
![Classes](<UML - Classes.jpeg>)

## Evaluation Calculator
![Evaluation Calculator](<UML - EvaluationCalculator.jpeg>)

## Similarity Calculator
![Similarity Calculator](<UML - SimilarityCalculator.jpeg>)

# Project Profiles

## Development and Testing Profile
 - test
 - H2 Database

### How to Use

1. Clone the project to your preferred IDE.
2. Run the Spring Boot project with `spring.profiles.active=${APP_PROFILE:test}` in the `application.properties`.
3. Create the available [SEED](<import.sql>).
4. Import the collection into Postman using the file [Collection](<Recommender-System-PE/postman/Recommender-System-PE.postman_collection.json>).
5. Use the provided routes to make requests to the system, remembering to pass the token generated during authentication.
6. Check the documentation on Swagger by accessing: [Swagger UI](http://localhost:8080/swagger-ui/index.html).
7. In the terminal, navigate to the repository folder and run the following Maven command to execute the tests:

```sh
mvn clean test
```

The JaCoCo reports, including the HTML version, and the Surefire reports in XML will be automatically generated.
 
 JaCoCo Report Path:
    
    Recommender-System-PE/target/site/jacoco/index.html

 In the terminal, run the following Maven command to generate the Surefire HTML report:

```sh
mvn site 
```

Surefire Report Path:

    Recommender-System-PE/target/site/surefire-report.html

The first time you run mvn site, it may take a while to download the Maven dependencies. 

## Staging Profile
 - dev
 - MySQL

### How to Use

1. Clone the project to your preferred IDE.
2. Open MySQL Workbench
   - Ensure the properties in `application-dev.properties` are correct.
3. Run the Spring Boot project with `spring.profiles.active=${APP_PROFILE:dev}` in the `application.properties`.
4. Create the available [SEED](<import.sql>).
5. Import the collection into Postman using the file [Collection](<Recommender-System-PE/postman/Recommender-System-PE.postman_collection.json>).
6. Use the provided routes to make requests to the system, remembering to pass the token generated during authentication.
7. Check the documentation on Swagger by accessing: [Swagger UI](http://localhost:8080/swagger-ui/index.html).
8. In the terminal, navigate to the repository folder and run the following Maven command to execute the tests:

```sh
mvn clean test
```

The JaCoCo reports, including the HTML version, and the Surefire reports in XML will be automatically generated.
 
 JaCoCo Report Path:
    
    Recommender-System-PE/target/site/jacoco/index.html

 In the terminal, run the following Maven command to generate the Surefire HTML report:

```sh
mvn site 
```

Surefire Report Path:

    Recommender-System-PE/target/site/surefire-report.html

The first time you run mvn site, it may take a while to download the Maven dependencies. 


## References

Aggarwal, C. C. 2016. Recommender Systems: The Textbook. Springer, Cham, ZH, Switzerland.

Borràs, J.; Moreno, A.; Valls, A. 2014. Intelligent tourism recommender systems: a survey. Expert Systems with Applications 41(16): 7370–7389.

Campos, J.; Roldão, I. de; Alves, P.; Dias, M.; Moura, B.; Freire, K.; Santos, N. C. dos; Freitas, P.; Braga, A.; Galvão, B.; Simões, F.; Jarocki, I.; Paulo, J.; Almeida, L.; Marques, P.; Rayane, S.; Xavier, S.; Instituto de Assessoria para o Desenvolvimento Humano [IADH]; Rede Nacional de Experiências e Turismo Criativo [RECRIA]. 2022. Plano de Turismo Criativo 2022-2024. Prefeitura do Recife, Recife, PE, Brasil.

Castells, P.; Jannach, D. 2023. Recommender systems: a primer. In: Alonso, O.; Baeza-Yates, R. (Eds.). Advanced Topics for Information Retrieval. ACM Press, New York, NY, USA.

Costa, I. 2024. Carnaval do Recife movimentou R$2,4 bilhões e recebeu mais de 3,4 milhões de foliões; prefeitura cogita juntar Data Magna aos dias oficiais de folia em 2025. In: G1, 2024, Brasil. Disponível em: https://g1.globo.com/pe/pernambuco/carnaval/2024/noticia/2024/02/14/balanco-do-carnaval-do-recife.ghtml. Acesso em: 04 de dezembro de 2024.

Danielsson, P. 1980. Euclidean distance mapping. Computer Graphics and Image Processing 14: 227–248.

Deshpande, M.; Karypis, G. 2004. Item-based top-N recommendation algorithms. ACM Transactions on Information Systems 22(1): 143–177.

Fernandes, E. M. 1999. Estatística aplicada. Universidade do Minho, Braga, Portugal.

Fonseca, D. 2020. Pernambuco lança passaporte para incentivar turismo no estado entre moradores e turistas. G1, 21 dez. 2020. Disponível em: https://g1.globo.com/pe/pernambuco/noticia/2020/12/21/passaporte-pernambuco-busca-incentivar-turismo-no-estado-entre-moradores-e-turistas.ghtml. Acesso em: 28 set. 2024.

Gao, M.; Liu, K.; Wu, Z. 2010. Personalisation in web computing and informatics: the techniques, applications, and future research. Information Systems Frontiers 12: 607–629.

Jesse, M.; Jannach, D.; Zangerle, E. 2023. Supporting characteristics of useful recommendations. In: RecSys 2023 - 17th ACM Conference on Recommender Systems, Singapore, Sept 18-22, 2023.

Li, Y.; Liu, K.; Wang, S.; Cambria, E. 2021. Recent developments in recommender systems: a survey. Journal of LaTeX Class Files 14(8).

Morais, I.; Mendonça, E.; Santos, E. 2022. Novas formas de fazer turismo: desde a prática às políticas na construção do Plano de Turismo Criativo do Recife (Pernambuco-Brasil). Interações 23(3): 669–684.

Qaiser, S.; Ali, R. 2018. Text mining: use of TF-IDF to examine the relevance of words to documents. International Journal of Computer Applications 181(1): 25–29.

Ramos, J. 2003. Using TF-IDF to determine word relevance in document queries. n.d.

Salton, G.; Buckley, C. 1988. Term-weighting approaches in automatic text retrieval. Information Processing & Management 24(5): 513–523.

Secretaria de Turismo e Lazer do Estado de Pernambuco [SETUR-PE]. 2020. Principais atrativos da cidade já contam com carimbos para o Passaporte Pernambuco. Recife, PE. Disponível em: https://www2.recife.pe.gov.br/noticias/08/12/2020/principais-atrativos-da-cidade-ja-contam-com-carimbos-para-o-passaporte. Acesso em: 04 dez. 2024.

Sondur, S. D.; Chigadani, A. P. 2016. Similarity measures for recommender systems: a comparative study. Journal for Research 2(3): 76–80.

Valença, J. 2022. Passaporte Pernambuco: saiba onde e como conseguir o caderno turístico. JC, 02 fev. 2022. Disponível em: https://jc.ne10.uol.com.br/blogs/turismo-de-valor/2022/02/14952400-passaporte-pernambuco-saiba-onde-e-como-conseguir-o-caderno-turistico.html. Acesso em: 04 dez. 2024.
