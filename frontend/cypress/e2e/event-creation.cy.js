/// <reference types="cypress" />
describe('Avaleht ja ürituse lisamine', () => {
  beforeEach(() => {
    cy.testsCleanup();
  });

  it('Mõlemad lingid Navbaris töötavad', () => {
    cy.visit('http://localhost:5173/');
    cy.get('.navbar-nav a').should('have.length', 2).then((items) => {
      cy.wrap(items[0]).as('homepageLink').should('contain.text', 'Avaleht');
      cy.wrap(items[1]).as('addEventLink').should('contain.text', 'Ürituse lisamine');

      cy.get('@addEventLink').click();
      cy.location('pathname').should('equal', '/add-event');

      cy.get('@homepageLink').click();
      cy.location('pathname').should('equal', '/');
    });
  });

  it('Kasutaja võib lisada uus üritus, vaata ürituse osavõtjad ja kustuta üritus', () => {
    cy.visit('http://localhost:5173/');
    cy.contains('a', 'Ürituse lisamine').click();

    cy.get('.submit-btn').click();

    cy.get('#eventName').as('eventNameInput').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');
    cy.get('#timestamp').as('timestampInput').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');
    cy.get('#place').as('placeInput').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');
    cy.location('pathname').should('equal', '/add-event');

    cy.get('@eventNameInput').type('Rahvuspidu');

    cy.get('@timestampInput').type('2023-01-01T17:12');
    cy.get('@timestampInput').should('have.class', 'is-invalid').parent().contains('Toimumisaeg peab olema tulevikus');

    const currentDate = new Date();
    currentDate.setDate(currentDate.getDate() + 10);
    cy.get('@timestampInput').type(currentDate.toISOString().slice(0, 16));

    cy.get('@placeInput').type('Kuskil Tallinnas');
    cy.get('#info').type('Mingi lisainfo');
    cy.contains('span', '14 / 1000');

    cy.contains('button', 'Lisa').click();

    cy.contains('Tulevased üritused').parent().within(() => {
      cy.get('.grid').should('have.length', 1).first().within(() => {
        cy.get('#indexCol').should('contain.text', '1.');
        cy.get('#nameCol').should('contain.text', 'Rahvuspidu');

        const formattedDate = currentDate.toLocaleDateString('et-EE', { day: '2-digit', month: '2-digit', year: 'numeric' });
        cy.get('#dateCol').should('contain.text', formattedDate);

        cy.get('#actionCol').within(() => {
          cy.get('#participantsBtn').click();
          cy.location('pathname').should('equal', '/event/1/participants');
          cy.go('back');
        });
      });

      cy.get('#deleteBtn').click();
      cy.get('.grid').should('not.exist');
    });
  });

  it('Toimunud üritus ei saa kustutada', () => {
    cy.intercept('GET', '**/api/events/past', [{ id: 1, name: 'Rahvuspidu', date: '29.01.2024' }]);
    cy.visit('http://localhost:5173/');

    cy.contains('Toimunud üritused').parent().within(() => {
      cy.get('.grid').should('have.length', 1).first().within(() => {
        cy.get('#indexCol').should('contain.text', '1.');
        cy.get('#nameCol').should('contain.text', 'Rahvuspidu');
        cy.get('#dateCol').should('contain.text', '29.01.2024');
        cy.get('#actionCol').within(() => {
          cy.get('#deleteBtn').should('not.exist');
        });
      });
    });
  });
});
