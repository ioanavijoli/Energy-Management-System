import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import Chart from 'chart.js/auto';

const EnergyConsumptionPage = () => {
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [devices, setDevices] = useState([]);
    const [selectedDevice, setSelectedDevice] = useState(null);
    const [energyData, setEnergyData] = useState([]);
    const chartRef = useRef(null);

    useEffect(() => {
        fetchDevices();
    }, []);

    const fetchDevices = async () => {
        try {
            const response = await axios.get('http://localhost:8081/user/devices', {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('token')
                },
            });
            setDevices(response.data);
        } catch (error) {
            console.error('Error fetching devices:', error);
        }
    };

    const handleDeviceChange = (deviceId) => {
        setSelectedDevice(deviceId);
    };

    const handleDateChange = (e) => {
        const selectedDateTime = new Date(e.target.value);
        setSelectedDate(selectedDateTime);
    };

    const fetchData = async () => {
        try {
            const formattedDate = new Date(selectedDate);
            formattedDate.setHours(23, 59, 59);

            const year = formattedDate.getFullYear();
            const month = ('0' + (formattedDate.getMonth() + 1)).slice(-2);
            const day = ('0' + formattedDate.getDate()).slice(-2);

            const formattedDateTime = `${year}-${month}-${day}T00:00:00`;
            const formattedEndTime = `${year}-${month}-${day}T23:59:59`;

            const response = await axios.get('http://localhost:8082/history/energyconsumption', {
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('token')
                },
                params: {
                    deviceId: selectedDevice,
                    dateTime: formattedDateTime,
                    endTime: formattedEndTime,
                },
            });
            setEnergyData(response.data);
        } catch (error) {
            console.error('Error fetching energy consumption data:', error);
        }
    };

    useEffect(() => {
        if (selectedDevice && selectedDate) {
            fetchData();
        }
    }, [selectedDevice, selectedDate]);

    useEffect(() => {
        if (energyData.length > 0) {
            renderChart();
        }
    }, [energyData]);

    useEffect(() => {
        if (chartRef.current !== null) {
            chartRef.current.destroy();
            renderChart();
        }
    }, [selectedDate]);

    const renderChart = () => {
        const labels = energyData.map((data) => {
            const date = new Date(data.dateTime);
            return date.getHours().toString();
        });
        const values = energyData.map((data) => data.consumption);

        const ctx = document.getElementById('energyChart').getContext('2d');

        if (chartRef.current) {
            chartRef.current.destroy();
        }

        chartRef.current = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Energy Consumption',
                        data: values,
                        fill: false,
                        borderColor: 'rgb(75, 192, 192)',
                        tension: 0.1,
                    },
                ],
            },
            options: {
                scales: {
                    x: {
                        title: {
                            display: true,
                            text: 'Time (Hour)',
                            color: 'white',
                        },
                        ticks: {
                            color: 'white',
                        },
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Energy Consumption (kWh)',
                            color: 'white',
                        },
                        ticks: {
                            color: 'white',
                        },
                    },
                },
            },
        });
    };

    return (
        <div>
            <h1 style={{ color: 'white' }}>Energy Consumption</h1>
            <div>
                <label htmlFor="calendar" style={{ color: 'white' }}>Select Date:</label>
                <input
                    type="date"
                    id="calendar"
                    value={selectedDate.toISOString().split('T')[0]}
                    onChange={handleDateChange}
                />
            </div>
            <div>
                <label htmlFor="deviceDropdown" style={{ color: 'white' }}>Select Device:</label>
                <select id="deviceDropdown" onChange={(e) => handleDeviceChange(e.target.value)}>
                    <option value="">Select</option>
                    {Array.isArray(devices) && devices.length > 0 && devices.map((device) => (
                        <option key={device.id} value={device.id}>
                            {device.id}
                        </option>
                    ))}
                </select>
            </div>
            <div>
                <h3 style={{ color: 'white' }}>Energy Consumption Chart</h3>
                <canvas id="energyChart" width="800" height="400"></canvas>
            </div>
        </div>
    );
};

export default EnergyConsumptionPage;
