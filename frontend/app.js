// --- Data Storage (Simulating the Backend) ---
let animals = [];
let enclosures = [];
let staff = [ // Pre-populate with default staff
    { id: 101, name: "Dr. Alice", role: "Doctor", username: "doc" },
    { id: 102, name: "Bob", role: "Zookeeper", username: "zoo" }
];
let animalIdCounter = 1;
let enclosureIdCounter = 1;
let staffIdCounter = 103;

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
    
    if (divId === 'viewAnimalsDiv') updateAnimalListView();
    if (divId === 'viewEnclosuresDiv') updateEnclosureListView();
}

function showActionDivForStaff(divId) {
    document.querySelectorAll('#staffManagementPanel .subpanel').forEach(div => div.style.display = 'none');
    document.getElementById(divId).style.display = 'block';

    if (divId === 'viewStaffDiv') updateStaffListView();
}


// --- Animal Management Functions ---
function addAnimal() {
    const name = document.getElementById('animalName').value;
    const type = document.getElementById('animalType').value;
    const age = parseInt(document.getElementById('animalAge').value);
    const option = document.getElementById('addAnimalOption').value;

    if (!name || !type || !age) {
        alert("Please fill all fields.");
        return;
    }

    const speciesExists = animals.some(a => a.name.toLowerCase() === name.toLowerCase());
    if (option === 'existing' && !speciesExists) {
        alert(`Species '${name}' does not exist. Please add as 'new' first.`);
        return;
    }

    const newAnimal = { id: animalIdCounter++, name, type, age, enclosureId: null };
    animals.push(newAnimal);
    alert(`Success! ${name} (ID: ${newAnimal.id}) was added.`);
    updateAnimalListView();
}

function removeAnimal() {
    const id = parseInt(document.getElementById('removeAnimalId').value);
    const animalIndex = animals.findIndex(a => a.id === id);

    if (animalIndex === -1) {
        alert("Animal not found.");
        return;
    }
    
    const removedAnimal = animals.splice(animalIndex, 1)[0];

    enclosures.forEach(enc => {
        const indexInEnclosure = (enc.animals || []).findIndex(animalId => animalId === id);
        if (indexInEnclosure > -1) {
            enc.animals.splice(indexInEnclosure, 1);
        }
    });

    alert(`Animal ${removedAnimal.name} (ID: ${id}) has been removed.`);
    updateAnimalListView();
    updateEnclosureListView();
}

function assignAnimal() {
    const animalId = parseInt(document.getElementById('assignAnimalId').value);
    const enclosureId = parseInt(document.getElementById('assignEnclosureId').value);

    const animal = animals.find(a => a.id === animalId);
    const enclosure = enclosures.find(e => e.id === enclosureId);

    if (!animal || !enclosure) {
        alert("Animal or Enclosure not found.");
        return;
    }
    
    if (animal.enclosureId) {
         const oldEnclosure = enclosures.find(e => e.id === animal.enclosureId);
         if (oldEnclosure) {
             const index = oldEnclosure.animals.indexOf(animal.id);
             oldEnclosure.animals.splice(index, 1);
         }
    }

    animal.enclosureId = enclosure.id;
    if (!enclosure.animals) enclosure.animals = [];
    enclosure.animals.push(animal.id);
    alert(`${animal.name} assigned to Enclosure ${enclosure.type}.`);
    updateEnclosureListView();
}

function addEnclosure() {
    const type = document.getElementById('enclosureType').value;
    if (!type) {
        alert("Please enter an enclosure type.");
        return;
    }
    const newEnclosure = { id: enclosureIdCounter++, type, animals: [], assignedStaff: [] };
    enclosures.push(newEnclosure);
    alert(`Enclosure '${type}' (ID: ${newEnclosure.id}) created.`);
    updateEnclosureListView();
}

// --- Staff Management Functions ---
function addStaff() {
    const role = document.getElementById('staffRole').value;
    const name = document.getElementById('staffName').value;
    const username = document.getElementById('staffUsername').value;

    if (!name || !username) {
        alert("Please provide a name and username.");
        return;
    }
    const newStaff = { id: staffIdCounter++, name, role, username };
    staff.push(newStaff);
    alert(`${role} ${name} (ID: ${newStaff.id}) has been added.`);
    updateStaffListView();
}

function removeStaff() {
    const id = parseInt(document.getElementById('removeStaffId').value);
    const staffIndex = staff.findIndex(s => s.id === id);

    if (staffIndex === -1) {
        alert("Staff member not found.");
        return;
    }
    
    const removedStaff = staff.splice(staffIndex, 1)[0];
    
    enclosures.forEach(enc => {
        const indexInEnclosure = (enc.assignedStaff || []).findIndex(staffId => staffId === id);
        if (indexInEnclosure > -1) {
            enc.assignedStaff.splice(indexInEnclosure, 1);
        }
    });
    
    alert(`Staff member ${removedStaff.name} (ID: ${id}) has been removed.`);
    updateStaffListView();
    updateEnclosureListView();
}

function assignStaff() {
    const staffId = parseInt(document.getElementById('assignStaffId').value);
    const enclosureId = parseInt(document.getElementById('assignStaffEnclosureId').value);

    const staffMember = staff.find(s => s.id === staffId);
    const enclosure = enclosures.find(e => e.id === enclosureId);

    if (!staffMember || !enclosure) {
        alert("Staff or Enclosure not found.");
        return;
    }
    
    if (!enclosure.assignedStaff) enclosure.assignedStaff = [];

    if (enclosure.assignedStaff.includes(staffId)) {
        alert(`${staffMember.name} is already assigned to this enclosure.`);
        return;
    }

    enclosure.assignedStaff.push(staffId);
    alert(`${staffMember.name} assigned to Enclosure ${enclosure.type}.`);
    updateEnclosureListView();
}


// --- View Update Functions ---
function updateAnimalListView() {
    const list = document.getElementById('animalList');
    list.innerHTML = '';
    if (animals.length === 0) {
        list.innerHTML = '<li>No animals found.</li>';
        return;
    }
    animals.forEach(a => {
        const li = document.createElement('li');
        li.textContent = `ID: ${a.id} | Name: ${a.name} | Type: ${a.type}`;
        list.appendChild(li);
    });
}

function updateEnclosureListView() {
    const list = document.getElementById('enclosureList');
    list.innerHTML = '';
    if (enclosures.length === 0) {
        list.innerHTML = '<li>No enclosures found.</li>';
        return;
    }
    enclosures.forEach(e => {
        const li = document.createElement('li');
        
        const animalCounts = {};
        (e.animals || []).forEach(animalId => {
            const animal = animals.find(a => a.id === animalId);
            if (animal) animalCounts[animal.name] = (animalCounts[animal.name] || 0) + 1;
        });
        const animalDetails = Object.entries(animalCounts).map(([name, count]) => `${name} (x${count})`).join(', ') || 'Empty';
        
        const staffDetails = (e.assignedStaff || [])
            .map(staffId => {
                const staffMember = staff.find(s => s.id === staffId);
                return staffMember ? staffMember.name : '';
            })
            .filter(name => name)
            .join(', ') || 'None';

        li.innerHTML = `<b>ID: ${e.id} | ${e.type}</b><br>
                        &nbsp;&nbsp;Animals: ${animalDetails}<br>
                        &nbsp;&nbsp;Staff: ${staffDetails}`;
        list.appendChild(li);
    });
}

function updateStaffListView() {
    const list = document.getElementById('staffList');
    list.innerHTML = '';
    if (staff.length === 0) {
        list.innerHTML = '<li>No staff members found.</li>';
        return;
    }
    staff.forEach(s => {
        const li = document.createElement('li');
        li.textContent = `ID: ${s.id} | Name: ${s.name} | Role: ${s.role}`;
        list.appendChild(li);
    });
}


// --- Doctor & Zookeeper Functions ---
function performCheckup() {
    const id = parseInt(document.getElementById('checkupAnimalId').value);
    const animal = animals.find(a => a.id === id);
    const msg = document.getElementById('doctorMessage');
    if (animal) {
        msg.textContent = `Performing checkup on ${animal.name} (ID: ${id}). Health records updated.`;
    } else {
        msg.textContent = "Animal not found.";
    }
}

function feedAnimal() {
    const id = parseInt(document.getElementById('feedAnimalId').value);
    const animal = animals.find(a => a.id === id);
    const msg = document.getElementById('zookeeperMessage');
    if (animal) {
        msg.textContent = `${animal.name} (ID: ${id}) has been fed according to its routine.`;
    } else {
        msg.textContent = "Animal not found.";
    }
}