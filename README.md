## License

This project is licensed under the [MIT License](https://github.com/douglasfragoso/intensivo-java-spring/blob/main/LICENSE), with the following additional restrictions:

- **Copying the code** for use in other projects without prior authorization is **not permitted**.
- **This project is not open source**, meaning the source code cannot be redistributed or modified without explicit permission.

[![NPM](https://img.shields.io/npm/l/react)](https://github.com/douglasfragoso/intensivo-java-spring/blob/main/LICENSE)

# A Point of Interest Recommendation System in Pernambuco with Java and Spring Boot

**Douglas Inácio Fragoso Ferreira¹\***; **Everton Gomede²**

1. University of São Paulo (USP). MBA in Software Engineering, Luiz de Queiroz College of Agriculture (Esalq), Pecege.
2. School of Electrical and Computer Engineering, State University of Campinas (FEEC/UNICAMP). PhD in Computer Science.

\*Corresponding author: douglas.iff@gmail.com

To calculate similarity metrics, a combination of TF-IDF, ED, COS, and PCC is used, along with evaluation metrics composed of Precision@k, Recall@k, F1 Score@k, Hit Rate@k, and Item Coverage. These metrics are described in detail in the subsections below.

---

## Technologies Used

| **Technology**                     | **Function**                                                                 |
|------------------------------------|-----------------------------------------------------------------------------|
| Java, Spring Boot, and Maven       | For API development                                                         |
| Spring Security and JSON Web Token | For security implementation                                                 |
| Spring Web MVC                     | For REST and MVC architecture                                               |
| Spring Data JPA, H2 Database, MySQL, and MySQL Connector | For data storage and manipulation                              |
| Spring Doc and Swagger             | For API documentation                                                       |
| Java Bean Validation and Lombok    | For code optimization                                                       |
| Spring Test, JUnit5, Mockito, TestContainers, Rest-Assured, and Docker | For unit and integration testing                               |
| JaCoCo, Maven Surefire Plugin, and Maven Surefire Report Plugin | For code coverage and test reporting                             |
| Postman                            | For manual endpoint testing                                                 |
| Git and GitHub                     | For code versioning                                                         |
| Visual Studio Code                 | For code editing                                                            |

---

## Similarity Metrics

The calculation of similarity metrics begins with **TF-IDF**, a numerical statistical technique that determines the relevance of a term in a set of documents (Qaiser and Ali, 2018; Salton and Buckley, 1988; Ramos, 2003). Salton and Buckley (1988) consider the TF-IDF algorithm simple and efficient, where TF is calculated as shown in equations (1), (2), and (3):

    TF(t, d) = freq(t) / (total number of words in the document) (1)

    IDF(t) = log(|D| / DF(t)) = log((total number of documents) / (number of documents containing term t)) (2)

    TF-IDF(t, d) = TF(t, d) * IDF(t) (3)



With the vector values, it is possible to obtain the similarity between them by normalizing the term weights using the cosine of the angle, demonstrating how relevant the term is in a set of documents (Salton and Buckley, 1988; Sondur and Chigadani, 2016).

To calculate **COS**, the product of the vectors is obtained, followed by normalizing the query and document vectors, and finally dividing the results (Gao et al., 2010; Salton and Buckley, 1988), as shown in equations (4), (5), and (6):

    ∑(k=1 to n) W_qk * W_dk (4)
    
    |Q| = √(∑(k=1 to n) (W_qk)^2), |D| = √(∑(k=1 to n) (W_dk)^2) (5)
    
    similarity(Q, D) = cos(W_qk, W_dk) = (∑(k=1 to n) W_qk * W_dk) / (√(∑(k=1 to n) (W_qk)^2 * √(∑(k=1 to n) (W_dk)^2) (6)

On the other hand, **ED** calculates the distance between two points through the square root of the sum of the squares of the differences of the vectors \(Q = (q_1, q_2, \dots, q_n)\) and \(D = (d_1, d_2, \dots, d_n)\), which represent the relevance of terms in documents based on TF-IDF (Danielsson, 1980; Sondur and Chigadani, 2016), as shown in equation (7):

    ED(Q, D) = √(∑(k=1 to n) (q_k - d_k)^2) (7)

ED is inversely proportional (Sondur and Chigadani, 2016), so the smaller the result, the more similar the terms are. To obtain an indicator similar to the COS metric, ED must be inverted, as in equation (8):

    similarity(Q, D) = Inverse ED(Q, D) = 1 / (1 + Euclidean distance(q, d)) (8)

Thus, the Euclidean similarity indicates that the higher the result, the greater the similarity between the vectors.

Finally, **PCC**, a metric used to measure the strength of the linear relationship between two vectors (Sondur and Chigadani, 2016), helps capture how the relative frequencies of terms in documents are correlated. To calculate PCC, the mean of vectors \(Q\) and \(D\) is obtained, followed by measuring the covariance and standard deviation between the vectors, as in equations (9), (10), (11), and (12):

    Q_med = (1/n) * ∑(k=1 to n) q_k, D_med = (1/n) * ∑(k=1 to n) d_k (9)

    ∑(k=1 to n) (q_k - Q_med)(d_k - D_med) (10)

    √(∑(k=1 to n) (q_k - Q_med)^2) * √(∑(k=1 to n) (d_k - D_med)^2) (11)

    Pearson Correlation Coefficient = r(Q, D) = (∑(k=1 to n) (q_k - Q_med)(d_k - D_med)) / (√(∑(k=1 to n) (q_k - Q_med)^2 * √(∑(k=1 to n) (d_k - D_med)^2) (12)

The result of this calculation ranges from -1 to 1, where -1 indicates a perfect negative linear correlation and 1 indicates a perfect positive linear correlation (Sondur and Chigadani, 2016). Therefore, like ED, the result must be normalized, as seen in equation (13):

    Pearson similarity(Q, D) = (r(Q, D) + 1) / 2 (13)

Thus, Pearson similarity ranges from 0 to 1, where 0 indicates complete opposition and 1 indicates identical documents.

---

## Evaluation Metrics

The metrics used in this system are Item-Based Indicators in terms of order, aiming to evaluate the performance of the recommended item lists. The first metric used is **Precision@k**, which focuses on the quality of recommendations through the proportion of relevant items in the top-k recommended items (Li et al., 2021), as described in equation (14):

    Precision@k = (Relevant Items in Top-k) / k (14)

The **Recall@k** metric measures the proportion of relevant items recommended in the top-k relative to the total number of relevant items available (Li et al., 2021), as in equation (15):

    Recall@k = (Relevant Items Retrieved in Top-k) / (Total Relevant Items) (15)

**F1-Score@k** combines Precision@k and Recall@k into a single metric, reflecting the balance between quality and coverage (Li et al., 2021), as in equation (16):

    F1-Score@k = 2 * (Precision@k * Recall@k) / (Precision@k + Recall@k) (16)

The **Hit Rate@k** metric focuses on ensuring that each user receives something relevant in the top-k, verifying the proportion of users served (Deshpande and Karypis, 2004), as described in equation (17):

    Hit Rate@k = (Users with at least 1 relevant item in Top-k) / (Total Users) (17)


Finally, the **Item Coverage** metric focuses on the diversity of recommendations, measuring the proportion of recommendable items that were effectively recommended to all users (Castells and Jannach, 2023), as in equation (18):

    Item Coverage = (Number of Unique Recommended Items) / (Total Available Items) (18)

## Results of the project

The MVP of this project implemented a Point of Interest (POI) Recommender System (RS) in Recife, focusing on General POI Recommendation. The recommendations are based on the content of POI attributes and the user's profile, considering their preference model.

To achieve this, a comparison is made using similarity measures between the user's preference terms and the descriptions of the POIs. Finally, the evaluation of these recommendations is performed in a binary and explicit format (like and dislike), which is later analyzed using evaluation metrics.

The system flow begins with **User and Preference Registration** and **POI Registration**. The user registers their general information and preferences, such as specific interests (motivations, hobbies, and themes). This information is stored in the database with static attributes (personal data) and dynamic attributes (preferences and interaction history).

The POIs (such as museums, churches, and restaurants) are registered with attributes like name, description, and preference types. When the user requests a recommendation, the system compares the user's preferences with the POI descriptions using **TF-IDF** to calculate the relevance of the terms.

With the TF-IDF values, it is possible to calculate the **Similarity Metrics**, composed of **COS**, **ED**, and **PCC**, and then obtain the average of the three metrics. The resulting values are used to measure the proximity between the user's profile and the POIs. Subsequently, the most relevant POIs are ranked and returned as a top-k list to the user.

The user evaluates the recommended POIs as **like** or **dislike**. These evaluations are stored in the database and can be analyzed using the metrics **Precision@k**, **Recall@k**, **F1-Score@k**, **Hit Rate@k**, and **Item Coverage**.
## References

Castells, P.; Jannach, D. 2023. Recommeder Systems: A Primer. In Alonso, O. & Baeza-Yates, R. (Eds.). Advanced Topics for Information Retrieval. ACM Press.

Danielsson, P. 1980. Euclidean Distance Mapping. Computer Graphics and Image Processing 14: 227-248.

Deshpande, M.; Karypis, G. 2004. Item-based top-N recommendation algorithms. Transactions on Information Systems 22(1): 143-177.

Gao, M; Liu, K.; Wu, Z. 2010. Personalisation in web computing and informatics: The techniques, applications, and future research. Information Systems Frontiers 12: 607-629.

Li, Y.; Liu, K.; Wang, S.; Cambria, E. 2021. Recent Developments in Recommender Systems: A Survey. Journal of Latex Class Files 14(8).

Qaiser, S.; Ali, R. 2018. Text Mining: Use of TF-IDF to Examine the Relevance of Words to Documents. International Journal of Computer Applications 181(1): 25-29.

Ramos, J. 2003. Using TF-IDF to Determine Word Relevance in Document Queries. 

Salton, G.; Buckley, C. 1988. Term-Weighting Approaches in Automatic Text Retrieval. Information Processing & Management 24 (5): 513-523

Sondur, S. D.; Chigadani, A. P. 2016. Similarity Measures for Recommender Systems: A Comparative Study. Journal for Resarch 2(3): 76-80


# Project Profiles

## Development and Testing Profile
 - test
 - H2 Database

### How to Use

1. Clone the project to your preferred IDE.
2. Run the Spring Boot project with `spring.profiles.active=${APP_PROFILE:test}` in the `application.properties`.
3. Import the collection into Postman using the file [Collection](<Recommender-System-PE.postman_collection.json>).
4. Use the provided routes to make requests to the system, remembering to pass the token generated during authentication.
5. Check the documentation on Swagger by accessing: [Swagger UI](http://localhost:8080/swagger-ui/index.html).


## Staging Profile
 - dev
 - MySQL

### How to Use

1. Clone the project to your preferred IDE.
2. Open MySQL Workbench
   - Ensure the properties in [application-dev.properties](src/main/resources/application-dev.properties) are correct.
3. Run the Spring Boot project with `spring.profiles.active=${APP_PROFILE:dev}` in the `application.properties`.
4. Create the available seed.
5. Import the collection into Postman using the file [Collection](<Recommender-System-PE.postman_collection.json>).
6. Use the provided routes to make requests to the system, remembering to pass the token generated during authentication.
7. Check the documentation on Swagger by accessing: [Swagger UI](http://localhost:8080/swagger-ui/index.html).

Made with care by Douglas Fragoso 👊