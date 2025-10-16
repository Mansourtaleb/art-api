// Script pour initialiser la base de données avec des données de test
// Exécuter avec: mongosh printdigital_db seed-data.js

// Nettoyer les collections existantes
db.utilisateurs.deleteMany({});
db.categories.deleteMany({});
db.oeuvres.deleteMany({});
db.bannieres.deleteMany({});

print("🧹 Collections nettoyées");

// 1. UTILISATEURS
const adminId = new ObjectId();
const clientId = new ObjectId();
const artisteId = new ObjectId();

db.utilisateurs.insertMany([
    {
        _id: adminId,
        nom: "Admin Print&Digital",
        email: "admin@printdigital.tn",
        motDePasse: "$2a$10$rGxVJ6V5BXJx.VZ6vXZQ6.5J8K5QZhXjY6K5Q6K5Q6K5Q6K5Q6K5Q", // Admin123
        role: "ADMIN",
        telephone: "+216 70 123 456",
        dateInscription: new Date(),
        emailVerifie: true,
        adresses: []
    },
    {
        _id: clientId,
        nom: "Client Test",
        email: "client@test.tn",
        motDePasse: "$2a$10$rGxVJ6V5BXJx.VZ6vXZQ6.5J8K5QZhXjY6K5Q6K5Q6K5Q6K5Q6K5Q", // Client123
        role: "CLIENT",
        telephone: "+216 20 456 789",
        dateInscription: new Date(),
        emailVerifie: true,
        adresses: [
            {
                adresse: "15 Avenue Habib Bourguiba",
                ville: "Tunis",
                codePostal: "1000",
                pays: "Tunisie",
                parDefaut: true
            }
        ]
    },
    {
        _id: artisteId,
        nom: "Artiste Demo",
        email: "artiste@printdigital.tn",
        motDePasse: "$2a$10$rGxVJ6V5BXJx.VZ6vXZQ6.5J8K5QZhXjY6K5Q6K5Q6K5Q6K5Q6K5Q", // Artiste123
        role: "ARTISTE",
        telephone: "+216 25 789 123",
        dateInscription: new Date(),
        emailVerifie: true,
        adresses: []
    }
]);

print("✅ Utilisateurs créés (admin/client/artiste)");

// 2. CATÉGORIES
db.categories.insertMany([
    { nom: "Art Moderne", description: "Œuvres contemporaines", imageUrl: "https://placehold.co/300x200/ec4899/ffffff?text=Art+Moderne", actif: true, ordre: 1 },
    { nom: "Calligraphie", description: "Calligraphie arabe et française", imageUrl: "https://placehold.co/300x200/8b5cf6/ffffff?text=Calligraphie", actif: true, ordre: 2 },
    { nom: "Nature", description: "Paysages et nature", imageUrl: "https://placehold.co/300x200/14b8a6/ffffff?text=Nature", actif: true, ordre: 3 },
    { nom: "Abstrait", description: "Art abstrait moderne", imageUrl: "https://placehold.co/300x200/f59e0b/ffffff?text=Abstrait", actif: true, ordre: 4 },
    { nom: "Portrait", description: "Portraits artistiques", imageUrl: "https://placehold.co/300x200/10b981/ffffff?text=Portrait", actif: true, ordre: 5 }
]);

print("✅ Catégories créées");

// 3. OEUVRES
db.oeuvres.insertMany([
    {
        titre: "Coucher de soleil tunisien",
        description: "Magnifique coucher de soleil sur Sidi Bou Said",
        categorie: "Nature",
        prix: NumberDecimal("45.00"),
        quantiteDisponible: 25,
        artisteId: artisteId.toString(),
        artisteNom: "Artiste Demo",
        images: ["https://placehold.co/400x400/ec4899/ffffff?text=Sunset"],
        statut: "DISPONIBLE",
        dateCreation: new Date(),
        avis: [],
        notemoyenne: 0
    },
    {
        titre: "Calligraphie arabe - Paix",
        description: "Belle calligraphie du mot 'Salam'",
        categorie: "Calligraphie",
        prix: NumberDecimal("35.00"),
        quantiteDisponible: 30,
        artisteId: artisteId.toString(),
        artisteNom: "Artiste Demo",
        images: ["https://placehold.co/400x400/8b5cf6/ffffff?text=Salam"],
        statut: "DISPONIBLE",
        dateCreation: new Date(),
        avis: [],
        notemoyenne: 0
    },
    {
        titre: "Médina de Tunis",
        description: "Vue artistique de la médina",
        categorie: "Art Moderne",
        prix: NumberDecimal("55.00"),
        quantiteDisponible: 15,
        artisteId: artisteId.toString(),
        artisteNom: "Artiste Demo",
        images: ["https://placehold.co/400x400/14b8a6/ffffff?text=Medina"],
        statut: "EN_PROMOTION",
        dateCreation: new Date(),
        avis: [],
        notemoyenne: 0
    },
    {
        titre: "Abstrait Coloré",
        description: "Composition abstraite vibrante",
        categorie: "Abstrait",
        prix: NumberDecimal("40.00"),
        quantiteDisponible: 20,
        artisteId: artisteId.toString(),
        artisteNom: "Artiste Demo",
        images: ["https://placehold.co/400x400/f59e0b/ffffff?text=Abstract"],
        statut: "DISPONIBLE",
        dateCreation: new Date(),
        avis: [],
        notemoyenne: 0
    },
    {
        titre: "Portrait Femme Tunisienne",
        description: "Portrait d'une femme en tenue traditionnelle",
        categorie: "Portrait",
        prix: NumberDecimal("65.00"),
        quantiteDisponible: 8,
        artisteId: artisteId.toString(),
        artisteNom: "Artiste Demo",
        images: ["https://placehold.co/400x400/10b981/ffffff?text=Portrait"],
        statut: "DISPONIBLE",
        dateCreation: new Date(),
        avis: [],
        notemoyenne: 0
    }
]);

print("✅ Oeuvres créées");

// 4. BANNIÈRES
db.bannieres.insertMany([
    {
        titre: "Promotion Ramadan",
        imageUrl: "https://placehold.co/1200x400/ec4899/ffffff?text=Promo+Ramadan",
        lienVers: "/oeuvres",
        typeLien: "EXTERNE",
        actif: true,
        ordre: 1,
        dateDebut: new Date(),
        dateFin: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000)
    },
    {
        titre: "Nouvelle Collection",
        imageUrl: "https://placehold.co/1200x400/8b5cf6/ffffff?text=Nouvelle+Collection",
        lienVers: "/oeuvres?categorie=Art+Moderne",
        typeLien: "EXTERNE",
        actif: true,
        ordre: 2,
        dateDebut: new Date(),
        dateFin: new Date(Date.now() + 60 * 24 * 60 * 60 * 1000)
    }
]);

print("✅ Bannières créées");

print("\n🎉 Base de données initialisée avec succès!");
print("\n📝 Comptes de test créés:");
print("   Admin:   admin@printdigital.tn / Admin123");
print("   Client:  client@test.tn / Client123");
print("   Artiste: artiste@printdigital.tn / Artiste123");
