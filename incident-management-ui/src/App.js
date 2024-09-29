import React, { useState, useEffect } from "react";
import axios from "axios";

// Set the base URL for Axios
axios.defaults.baseURL = process.env.REACT_APP_BACKEND_URL || 'http://localhost:8080';

function App() {
  const [incidents, setIncidents] = useState([]);
  const [createTitle, setCreateTitle] = useState("");
  const [createDescription, setCreateDescription] = useState("");
  const [createPriority, setCreatePriority] = useState("ONE");
  const [createStatus, setCreateStatus] = useState("OPEN");
  const [createError, setCreateError] = useState(""); // New state for error message

  const [updateId, setUpdateId] = useState(null);
  const [updateTitle, setUpdateTitle] = useState("");
  const [updateDescription, setUpdateDescription] = useState("");
  const [updatePriority, setUpdatePriority] = useState("ONE");
  const [updateStatus, setUpdateStatus] = useState("OPEN");

  const [searchId, setSearchId] = useState("");
  const [searchedIncident, setSearchedIncident] = useState(null);

  // Fetch all incidents on load
  useEffect(() => {
    getAllIncidents();
  }, []);

  const getAllIncidents = async () => {
    try {
      const response = await axios.get("/incidents");
      setIncidents(response.data);
    } catch (error) {
      console.error("Error fetching incidents:", error);
    }
  };

  // Create Incident with error handling for 409 Conflict
  const createIncident = async () => {
    try {
      setCreateError(""); // Clear previous errors
      await axios.post("/incidents", {
        title: createTitle,
        description: createDescription,
        priority: createPriority,
        status: createStatus,
      });
      setCreateTitle("");
      setCreateDescription("");
      setCreatePriority("ONE");
      setCreateStatus("OPEN");
      getAllIncidents();
    } catch (error) {
      if (error.response && error.response.status === 409) {
        // Handle 409 Conflict error
        setCreateError("Incident with the same title and description already exists.");
      } else {
        console.error("Error creating incident:", error);
      }
    }
  };

  // Update Incident
  const updateIncident = async () => {
    try {
      await axios.put(`/incidents/${updateId}`, {
        title: updateTitle,
        description: updateDescription,
        priority: updatePriority,
        status: updateStatus,
      });
      setUpdateId(null);
      setUpdateTitle("");
      setUpdateDescription("");
      setUpdatePriority("ONE");
      setUpdateStatus("OPEN");
      getAllIncidents();
    } catch (error) {
      console.error("Error updating incident:", error);
    }
  };

  // Select incident for updating
  const selectIncident = (incident) => {
    setUpdateId(incident.id); // Set ID but keep it immutable
    setUpdateTitle(incident.title);
    setUpdateDescription(incident.description);
    setUpdatePriority(incident.priority);
    setUpdateStatus(incident.status);
  };

  // Delete incident
  const deleteIncident = async (id) => {
    try {
      await axios.delete(`/incidents/${id}`);
      getAllIncidents();
    } catch (error) {
      console.error("Error deleting incident:", error);
    }
  };

  // Get incident by ID
  const getIncidentById = async () => {
    try {
      const response = await axios.get(`/incidents/${searchId}`);
      setSearchedIncident(response.data);
    } catch (error) {
      console.error("Error fetching incident by ID:", error);
      setSearchedIncident(null);
    }
  };

  return (
    <div className="App">
      <h1>Incident Management</h1>

      {/* Create Incident Section */}
      <div>
        <h2>Create Incident</h2>
        <input
          type="text"
          placeholder="Title"
          value={createTitle}
          onChange={(e) => setCreateTitle(e.target.value)}
        />
        <input
          type="text"
          placeholder="Description"
          value={createDescription}
          onChange={(e) => setCreateDescription(e.target.value)}
        />
        <select
          value={createPriority}
          onChange={(e) => setCreatePriority(e.target.value)}
        >
          <option value="ONE">ONE</option>
          <option value="TWO">TWO</option>
          <option value="THREE">THREE</option>
          <option value="FOUR">FOUR</option>
          <option value="FIVE">FIVE</option>
        </select>
        <select
          value={createStatus}
          onChange={(e) => setCreateStatus(e.target.value)}
        >
          <option value="OPEN">OPEN</option>
          <option value="IN_PROGRESS">IN_PROGRESS</option>
          <option value="CLOSED">CLOSED</option>
        </select>
        <button onClick={createIncident}>Create Incident</button>
        {/* Display error message if there is a conflict */}
        {createError && <p style={{ color: "red" }}>{createError}</p>}
      </div>

      {/* Update Incident Section */}
      <div>
        <h2>Update Incident</h2>
        {updateId ? (
          <>
            <div>
              <label>ID (Read-only):</label>
              <input
                type="text"
                value={updateId}
                readOnly
              />
            </div>
            <input
              type="text"
              placeholder="Title"
              value={updateTitle}
              onChange={(e) => setUpdateTitle(e.target.value)}
            />
            <input
              type="text"
              placeholder="Description"
              value={updateDescription}
              onChange={(e) => setUpdateDescription(e.target.value)}
            />
            <select
              value={updatePriority}
              onChange={(e) => setUpdatePriority(e.target.value)}
            >
              <option value="ONE">ONE</option>
              <option value="TWO">TWO</option>
              <option value="THREE">THREE</option>
              <option value="FOUR">FOUR</option>
              <option value="FIVE">FIVE</option>
            </select>
            <select
              value={updateStatus}
              onChange={(e) => setUpdateStatus(e.target.value)}
            >
              <option value="OPEN">OPEN</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="CLOSED">CLOSED</option>
            </select>
            <button onClick={updateIncident}>Update Incident</button>
          </>
        ) : (
          <p>Select an incident to update</p>
        )}
      </div>

      {/* Search Incident by ID Section */}
      <div>
        <h2>Get Incident by ID</h2>
        <input
          type="text"
          placeholder="Enter Incident ID"
          value={searchId}
          onChange={(e) => setSearchId(e.target.value)}
        />
        <button onClick={getIncidentById}>Get Incident</button>

        {searchedIncident ? (
          <div>
            <h3>Incident Details:</h3>
            <p>ID: {searchedIncident.id}</p>
            <p>Title: {searchedIncident.title}</p>
            <p>Description: {searchedIncident.description}</p>
            <p>Priority: {searchedIncident.priority}</p>
            <p>Status: {searchedIncident.status}</p>
          </div>
        ) : searchId && <p>No incident found with this ID</p>}
      </div>

      {/* List of Incidents */}
      <h2>All Incidents</h2>
      <ul>
        {incidents.map((incident) => (
          <li key={incident.id}>
            <strong>ID:</strong> {incident.id} | <strong>Title:</strong>{" "}
            {incident.title} | <strong>Description:</strong>{" "}
            {incident.description} | <strong>Priority:</strong>{" "}
            {incident.priority} | <strong>Status:</strong> {incident.status}
            <button onClick={() => selectIncident(incident)}>Edit</button>
            <button onClick={() => deleteIncident(incident.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
