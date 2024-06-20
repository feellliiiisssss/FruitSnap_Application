# Capstone Bangkit 2024 Batch 1

## Team ID : C241-PS333

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href=''><img src='https://github.com/Eislax/FruitSnap-ML/blob/main/Assets/logoCapStone.png' type='image' alt="Logo"></a>

  <h1 align="center">FruitSnap</h1>
  <h2 align="center">Empowering Freshness, Ensuring Quality</h2>
  
  <p align="center">
  This is a project to fulfill the  Final assesment of <a href="https://grow.google/intl/id_id/bangkit/"><strong>Bangkit Academy led by Google, Tokopedia, Gojek, & Traveloka</strong></a>
   Program.
    <br />
    <br />
    <a href="" target="_blank">Project Brief</a>
    ·
    <a href="" target="_blank">FruitSnap APK</a>
    ·
    <a href="" target="_blank">Video Presentation</a>
    ·
    <a href="" target="_blank">PPT Slide</a>
    ·
    <a href="" target="_blank">Go To Market Slide</a>
    <br />
    © C241-PS333 Bangkit Capstone Team
  </p>
</p>
<br>

# Team Members

<br>

| Name                              | Student ID   | Path               | Universitas                              |
| --------------------------------- | ------------ | ------------------ | ---------------------------------------- |
| Arlynandhita Felisya Putri Wibowo | M271D4KX3161 | Machine Learning   | Universitas Multimedia Nusantara Jakarta |
| Gibran Faktian Anwar              | M526D4KY2058 | Machine Learning   | Politeknik Negeri Indramayu              |
| Muhammad Rafi Ilham               | M009D4KY2513 | Machine Learning   | Universitas Gunadarma                    |
| M. Baharuddin Yusuf               | C279D4KY0755 | Cloud Computing    | Universitas Negeri Malang                |
| Wahyu Dwi Setio Wibowo            | C271D4KY0846 | Cloud Computing    | Universitas Multimedia Nusantara Jakarta |
| Dawson Flannery Susanto           | A009D4KY3516 | Mobile Development | Universitas Gunadarma                    |
| Raihan Fauzi                      | A009D4KY3897 | Mobile Development | Universitas Gunadarma                    |

<br>

## About The Project

<p>In the fruit industry, assessing fruit condition is crucial yet plagued by issues like inconsistent inspections, unreliable accuracy from manual processes, and high rejection rates due to rot. By 2050, the world's population is expected to surge by 40% to 9.7 billion, doubling the demand for fruits. However, agricultural employment is projected to halve, resulting in a shortage of 5 million harvesters. Compounding these challenges is the current reality that over 50% of fruits and vegetables go to waste due to inefficient and inaccurate distribution. </p>
<p>With those issues in mind, we aim to address these challenges by developing an application called "FruitSnap" for an automated system to detect fresh and rotten fruits using image analysis in machine learning techniques. Apart from classifying fruit, this application can also provide users with tips on how to properly maintain fruit.</p>

## Documentation

You can find our relevant documentation at the following link:

### Machine Learning

- [Machine Learning Documentation](https://github.com/Eislax/FruitSnap-ML)

### Cloud Computing

- [Cloud Computing Documentation](https://github.com/Eislax/FruitSnap_Application/tree/main/CC)

### Mobile Development

- [Mobile Development Documentation]()

## Contact

| Name                              | Contact                                                                                                                                                                         |
| --------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Arlynandhita Felisya Putri Wibowo | <a href="https://www.linkedin.com/in/arlynandhita-felisya/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a>    |
| Gibran Faktian Anwar              | <a href="https://www.linkedin.com/in/gibranfaktiananwar/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a>      |
| Muhammad Rafi Ilham               | <a href="https://www.linkedin.com/in/muhammad-rafi-ilham"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a>      |
| M. Baharuddin Yusuf               | <a href="https://www.linkedin.com/in/m-baharuddin/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a>            |
| Wahyu Dwi Setio Wibowo            | <a href="https://www.linkedin.com/in/wahyudsw03/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a>              |
| Dawson Flannery Susanto           | <a href="https://www.linkedin.com/in/dawson-flannery-susanto/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a> |
| Raihan Fauzi                      | <a href=""><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a>                                                     |

# Machine Learning FruitSnap Documentation
FruitSnap is an application that uses machine learning technology to classify images of fruits. It utilizes TensorFlow as the main framework for machine learning model building. Utilizing transfer learning techniques, the application uses InceptionV3's pre-trained model on ImageNet datasets, and then retrains the model on fruit-specific datasets to improve classification accuracy.

## Dataset
The data used to train and test our model is taken from Kaggle, is in the form of a zip file, and contains three folders namely train, test, and val. Within each of the train, test, and val folders are eight folders or classes, namely freshapples, freshbanana, freshoranges, freshstrawberry, rottenapples, rottenbanana, rottenoranges, and rottenstrawberry. This division of the dataset helps to ensure unbiased model evaluation.

## Requirements
To run the code in this repository, the following dependencies are required (better import all library needed from notebook file):

1. TensorFlow: A machine learning framework for building and training models.
2. Matplotlib: A plotting library for visualizing data.
3. NumPy: A library for numerical computations.
4. Kaggle API: A library to download datasets from Kaggle.

## Model Architecture

1. Data Augmentation: Uses ImageDataGenerator to perform image augmentation such as rotation, width and height shift, cropping, and zooming to increase the variety of training data.
2. Base Model: Using the InceptionV3 model pre-trained on the ImageNet dataset as a base. This layer is not retrained initially to utilize the features learned from the ImageNet dataset.
3. Custom Layers: Menambahkan beberapa lapisan di atas model pre-trained InceptionV3 untuk menyesuaikan dengan dataset buah-buahan:
   * Flatten: To flatten the output of the base model.
   * Dense: Fully connected layer with 1024 neurons and ReLU activation.
   * Dropout: With a dropout rate of 0.5 to reduce overfitting.
   * Dense: Output layer with the number of neurons corresponding to the number of classes (fruit categories) and softmax activation for classification.
5. Training: The model is trained with two phases:
   * First phase: Train the custom layer with the frozen base model.
   * Second phase: Unfreeze the final few layers of the base model and train the entire model with a lower learning rate for fine-tuning.


# Cloud Computing FruitSnap Documentation
## Authentication

### **Sign Up - Registration new user**

```http
  POST /register
```

| Parameter  | Type     | Description                             |
| :--------- | :------- | :-------------------------------------- |
| `username` | `string` | **Required**. username of user to fetch |
| `password` | `string` | **Required**. valid password            |

Response :

```json
{
  "message": "User registered successfully"
}
```

### **Sign In - Login**

```http
  POST /login
```

| Parameter  | Type     | Description                             |
| :--------- | :------- | :-------------------------------------- |
| `username` | `string` | **Required**. username of user to fetch |
| `password` | `string` | **Required**. valid password            |

Response :

```json
{
  "message": "User Success login successful"
}
```

# Fruit

### **Predict your image**

```http
  POST /predict
```

Header :

- **Content-Type:** application/json
- **Authorization:** Token

| Parameter | Type   | Description                                 |
| :-------- | :----- | :------------------------------------------ |
| `image`   | `file` | **Required**. the image you want to predict |

Response :

```json
{
  "predicted": "fresh_strawberry",
  "message": "Image has already been predicted",
  "timestamp": "Jun 12, 2024, 12:33:18.700 PM"
}
```

### **Get last history of predict**

```http
  GET /history
```

Header :

- **Content-Type:** application/json
- **Authorization:** Token

| Parameter | Type | Description |
| :-------- | :--- | :---------- |
|           |      |             |

Response :

```json
{
  "predicted": "fresh_strawberry",
  "timestamp": "Jun 12, 2024, 12:33:18.700 PM"
}
```
