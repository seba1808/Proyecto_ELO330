document.getElementById('ledButton').addEventListener('click', function() {
    var xhttp = new XMLHttpRequest();
    
    xhttp.open("GET", "/Boton", true);
    xhttp.send();
});
document.addEventListener('DOMContentLoaded', (event) => {
    const ctx = document.getElementById('temperatureChart').getContext('2d');
    const temperatureData = {
        labels: [],
        datasets: [{
            label: 'Señal de Interes',
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1,
            lineTension: 0.1,
            data: []
        }]
    };

    const config = {
        type: 'line',
        data: temperatureData,
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            },
            responsive: true,
            maintainAspectRatio: false
        }
    };

    const temperatureChart = new Chart(ctx, config);

    function addTemperatureData(chart, label, data) {
        if (chart.data.labels.length > 20) {
            chart.data.labels.shift();
            chart.data.datasets.forEach((dataset) => {
                dataset.data.shift();
            });
        }
        
        chart.data.labels.push(label);
        chart.data.datasets.forEach((dataset) => {
            dataset.data.push(data);
        });
        chart.update();
    }

    setInterval(() => {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                const currentTime = new Date().toLocaleTimeString();
                addTemperatureData(temperatureChart, currentTime, parseInt(this.responseText));
            }
        };
    
    
        xhttp.open("GET", "/Dinamico", true);
        xhttp.send();
    }, 1000);
});
