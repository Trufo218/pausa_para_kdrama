  // Import the functions you need from the SDKs you need
  import { initializeApp } from "https://www.gstatic.com/firebasejs/12.5.0/firebase-app.js";
  import { getAnalytics } from "https://www.gstatic.com/firebasejs/12.5.0/firebase-analytics.js";
  // TODO: Add SDKs for Firebase products that you want to use
  // https://firebase.google.com/docs/web/setup#available-libraries

  // Your web app's Firebase configuration
  // For Firebase JS SDK v7.20.0 and later, measurementId is optional
  const firebaseConfig = {
    apiKey: "AIzaSyC3mYaJLSEOU6yU3CZBa6uAUUMZrByt224",
    authDomain: "pausaparakdrama.firebaseapp.com",
    projectId: "pausaparakdrama",
    storageBucket: "pausaparakdrama.firebasestorage.app",
    messagingSenderId: "1041723137654",
    appId: "1:1041723137654:web:7b3fac057cb3cf2a6e0957",
    measurementId: "G-D83GTPWEM3"
  };

  // Initialize Firebase
  const app = initializeApp(firebaseConfig);
  const analytics = getAnalytics(app);