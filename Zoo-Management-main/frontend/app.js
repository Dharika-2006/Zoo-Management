// --- UI Navigation ---
function showPanel(panelId) {
  document.querySelectorAll('.panel').forEach(p => p.style.display = 'none');
  if (panelId === 'admin') {
    document.getElementById('adminPanel').style.display = 'block';
    document.getElementById('adminAreaSelection').style.display = 'block';
    document.getElementById('animalManagementPanel').style.display = 'none';
    document.getElementById('staffManagementPanel').style.display = 'none';
  } else if (panelId === 'roleSelection') {
    document.getElementById('roleSelection').style.display = 'block';
  } else {
    document.getElementById(panelId + 'Panel').style.display = 'block';
  }
}

function showSubPanel(subPanelId) {
  document.getElementById('adminAreaSelection').style.display = 'none';
  document.getElementById(subPanelId).style.display = 'block';
}

function showActionDiv(divId) {
  document.querySelectorAll('#animalManagementPanel .subpanel').forEach(div => div.style.display = 'none');
  document.getElementById(divId).style.display = 'block';
}

function showActionDivForStaff(divId) {
  document.querySelectorAll('#staffManagementPanel .subpanel').forEach(div => div.style.display = 'none');
  document.getElementById(divId).style.display = 'block';
}
// === Load animals from backend ===
async function loadAnimals() {
  try {
    const res = await fetch('http://localhost:8080/animals');
    const text = await res.text();
    document.getElementById('output').innerText = text;
  } catch (err) {
    console.error("Failed to load animals:", err);
    document.getElementById('output').innerText = "Error loading animals 😢";
  }
}

// === Load enclosures from backend ===
async function loadEnclosures() {
  try {
    const res = await fetch('http://localhost:8080/enclosures');
    const text = await res.text();
    document.getElementById('enclosuresOutput').innerText = text;
  } catch (err) {
    console.error("Failed to load enclosures:", err);
    document.getElementById('enclosuresOutput').innerText = "Error loading enclosures 😢";
  }
}

// === Add animal ===
async function addAnimal() {
  const name = document.getElementById('animalName').value;
  const type = document.getElementById('animalType').value;
  const age = parseInt(document.getElementById('animalAge').value);
  if (!name || !type || !age) return alert("Fill all fields");

  const body = `${type},${name},${age}`;
  const res = await fetch('http://localhost:8080/addAnimal', { method: 'POST', body });
  alert(await res.text());
  loadAnimals();
}

// === Remove animal ===
async function removeAnimal() {
  const id = document.getElementById('removeAnimalId').value;
  if (!id) return alert("Enter Animal ID");

  const res = await fetch('http://localhost:8080/removeAnimal', { method: 'POST', body: id });
  alert(await res.text());
  loadAnimals();
}

// === Add enclosure ===
async function addEnclosure() {
  const type = document.getElementById('enclosureType').value;
  if (!type) return alert("Please enter enclosure type");

  const res = await fetch('http://localhost:8080/addEnclosure', { method: 'POST', body: type });
  alert(await res.text());
  loadEnclosures();
}

// === Assign animal ===
async function assignAnimal() {
  const animalId = document.getElementById('assignAnimalId').value;
  const enclosureId = document.getElementById('assignEnclosureId').value;
  const body = `${animalId},${enclosureId}`;

  const res = await fetch('http://localhost:8080/assignAnimal', { method: 'POST', body });
  alert(await res.text());
  loadEnclosures();
}

// === Doctor performs checkup ===
async function performCheckup() {
  const id = document.getElementById('checkupAnimalId').value;
  const res = await fetch('http://localhost:8080/doctorCheckup', { method: 'POST', body: id });
  document.getElementById('doctorMessage').innerText = await res.text();
}

// === Zookeeper feeds animal ===
async function feedAnimal() {
  const id = document.getElementById('feedAnimalId').value;
  const res = await fetch('http://localhost:8080/feedAnimal', { method: 'POST', body: id });
  document.getElementById('zookeeperMessage').innerText = await res.text();
}

// === STAFF MANAGEMENT ===
async function addStaff() {
  const role = document.getElementById("staffRole").value;
  const name = document.getElementById("staffName").value;
  const username = document.getElementById("staffUsername").value;
  const password = document.getElementById("staffPassword").value;

  const response = await fetch("http://localhost:8080/addStaff", {
    method: "POST",
    body: `${role},${name},${username},${password}`
  });
  alert(await response.text());
  viewStaff();
}

async function removeStaff() {
  const id = document.getElementById("removeStaffId").value;
  const response = await fetch("http://localhost:8080/removeStaff", {
    method: "POST",
    body: id
  });
  alert(await response.text());
  viewStaff();
}

async function assignStaff() {
  const staffId = document.getElementById("assignStaffId").value;
  const enclosureId = document.getElementById("assignStaffEnclosureId").value;

  const response = await fetch("http://localhost:8080/assignStaff", {
    method: "POST",
    body: `${staffId},${enclosureId}`
  });
  alert(await response.text());
  loadEnclosures();
}

async function viewStaff() {
  const response = await fetch("http://localhost:8080/staff");
  document.getElementById("staffList").innerText = await response.text();
}

// === Initial load ===
window.onload = function() {
  loadAnimals();
  loadEnclosures();
};
