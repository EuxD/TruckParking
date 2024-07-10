document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("truckerForm").addEventListener("submit", function(event) {
        event.preventDefault(); // Prevent form from submitting the traditional way

        var id = document.getElementById("id").value;
        var trucker = handleTruckerRequestById(id);

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
    ol.style.setProperty("--length", truckers.length);
    resultsDiv.appendChild(ol);

    for (var i = 0; i < truckers.length; i++) {
        ol.appendChild(buildListItem(truckers[i], i + 1));
    }
}

function buildListItem(trucker, number) {
    const li = document.createElement("li");
    li.style.setProperty("--i", String(number));

    currentElement = document.createElement("h5");
    currentElement.innerText = "ID: " + trucker.id_trucker;
    currentElement.style.fontWeight = "bold"; // Applico un font-weight in più per evidenziare l'ID
    li.appendChild(currentElement);

    var currentElement = document.createElement("h5");
    currentElement.setAttribute("class", "name-class");
    currentElement.innerText = "Name: " + trucker.name;
    li.appendChild(currentElement);

    currentElement = document.createElement("h5");
    currentElement.innerText = "Surname: " + trucker.surname;
    currentElement.style.fontWeight = "bold";
    li.appendChild(currentElement);


    currentElement = document.createElement("h5");
    currentElement.setAttribute("class", "role-class");
    currentElement.innerText = "Role: " + trucker.role;
    li.appendChild(currentElement);

    return li;
}

function handleTruckerRequestById(id) {
    var xhttp = new XMLHttpRequest();
    var url = 'http://localhost:8080/truckparking/rest/trucker/ID/' + id;
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
    resultsDiv.innerHTML = "<p>No trucker found with the provided ID.</p>";
}
