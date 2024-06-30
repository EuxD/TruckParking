document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("truckerForm").addEventListener("submit", function(event) {
        event.preventDefault(); // Prevent form from submitting the traditional way

        var email = document.getElementById("email").value;
        var trucker = handleTruckerRequestByEmail(email);

        if (trucker) {
            buildList([trucker]);
        } else {
            displayNoResults();
        }
    });
});

function buildList(truckers) {
    const resultsDiv = document.getElementById("results");
    resultsDiv.innerHTML = ""; // Clear previous results

    const ol = document.createElement("ol");
    ol.style.setProperty("--length", "3");
    resultsDiv.appendChild(ol);

    for (var i = 0; i < truckers.length; i++) {
        ol.appendChild(buildListItem(truckers[i], i + 1));
    }
}

function buildListItem(trucker, number) {
    const li = document.createElement("li");
    li.style.setProperty("--i", String(number));

    var currentElement = document.createElement("h5");
    currentElement.setAttribute("class", "name-class");
    currentElement.innerText = "Name: " + trucker.name;
    li.appendChild(currentElement);

    currentElement = document.createElement("h5");
    currentElement.setAttribute("class", "email-class");
    currentElement.innerText = "Email: " + trucker.email;
    li.appendChild(currentElement);

    currentElement = document.createElement("h5");
    currentElement.setAttribute("class", "role-class");
    currentElement.innerText = "Role: " + trucker.role;
    li.appendChild(currentElement);

    // Add more details if needed

    return li;
}

function handleTruckerRequestByEmail(email) {
    var xhttp = new XMLHttpRequest();
    var url = 'http://localhost:8080/truckparking/rest/trucker/email/'+ email;
    xhttp.open("GET", url, false);
    xhttp.send();

    if (xhttp.status === 200) {
        return JSON.parse(xhttp.responseText);
    } else {
        console.log(`Error fetching trucker: ${xhttp.status}`);
        return null;
    }
}

function displayNoResults() {
    const resultsDiv = document.getElementById("results");
    resultsDiv.innerHTML = "<p>No trucker found with the provided email.</p>";
}
