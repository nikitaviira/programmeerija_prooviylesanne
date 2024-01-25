/// <reference types="cypress" />
describe('Osavõtjate lisamine ja muutmine', () => {
  beforeEach(() => {
    cy.testsCleanup();
    cy.saveEvent();
  });

  it('Kasutaja võib lisata uut füüsilist isikut, muuta füüsilise isiku andmeid ja kustutada füüsilise isiku', () => {
    cy.visit('http://localhost:5173/');

    cy.contains('Toimunud üritused').parent().within(() => {
      cy.get('#participantsBtn').click();
    });

    cy.get('#eventName').should('have.text', 'Sunnipaev');
    cy.get('#eventDateTime').should('have.text', '22.01.2024 17:01');
    cy.get('#eventPlace').should('have.text', 'Mustamae tee 11');
    cy.contains('Ei ole ühtegi osavõtja');

    cy.get('#eventPersonSelector').should('be.checked');

    cy.get('#personForm label').then((items) => {
      cy.wrap(items[0]).should('contain.text', 'Eesnimi');
      cy.wrap(items[1]).should('contain.text', 'Perenimi');
      cy.wrap(items[2]).should('contain.text', 'Isikukood');
      cy.wrap(items[3]).should('contain.text', 'Maksmise viis');
      cy.wrap(items[4]).should('contain.text', 'Lisainfo');
    });

    cy.contains('button', 'Salvesta').click();

    cy.get('#personFirstName').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');
    cy.get('#personLastName').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');
    cy.get('#personPersonalCode').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');
    cy.get('#personPaymentType').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');

    cy.get('#personFirstName').type('Nikita');
    cy.get('#personLastName').type('Viira');

    cy.get('#personPersonalCode').type('1123123123123213');
    cy.get('#personPersonalCode').should('have.class', 'is-invalid').parent().contains('Isikukood ei ole valiidne');
    cy.get('#personPersonalCode').type('{selectall}{backspace}39807093721');

    cy.get('#personPaymentType').select('Pangaülekanne');

    cy.get('#personInfo').type('Mingi lisainfo');
    cy.contains('span', '14 / 1500');

    cy.contains('button', 'Salvesta').click();

    cy.get('#indexCol').should('contain.text', '1.');
    cy.get('#fullNameCol').should('contain.text', 'Nikita Viira');
    cy.get('#codeCol').should('contain.text', '39807093721');
    cy.get('#actionCol').within(() => {
      cy.get('#participantDetailsBtn').click();
      cy.location('pathname').should('equal', '/person/1');
    });

    // Füsilist isiku detailide muutmine
    cy.get('#personFirstName').type('{selectall}{backspace}John');
    cy.contains('button', 'Salvesta').click();

    cy.contains('Toimunud üritused').parent().within(() => {
      cy.get('#participantsBtn').click();
    });

    cy.get('#fullNameCol').should('contain.text', 'John Viira');

    // Füsilist isiku kustutamine
    cy.get('#removeParticipantBtn').click();
    cy.contains('Ei ole ühtegi osavõtja');
  });

  it('Kasutaja võib lisata uut ettevõtte üritusele, muuta ettevõtte andmed ja kustuta ettevõtte', () => {
    cy.visit('http://localhost:5173/');

    cy.contains('Toimunud üritused').parent().within(() => {
      cy.get('#participantsBtn').click();
    });

    cy.get('#eventName').should('have.text', 'Sunnipaev');
    cy.get('#eventDateTime').should('have.text', '22.01.2024 17:01');
    cy.get('#eventPlace').should('have.text', 'Mustamae tee 11');
    cy.contains('Ei ole ühtegi osavõtja');

    cy.get('#eventCompanySelector').check();

    cy.get('#companyForm label').then((items) => {
      cy.wrap(items[0]).should('contain.text', 'Juriidiline nimi');
      cy.wrap(items[1]).should('contain.text', 'Registrikood');
      cy.wrap(items[2]).should('contain.text', 'Osavõtjate arv');
      cy.wrap(items[3]).should('contain.text', 'Maksmise viis');
      cy.wrap(items[4]).should('contain.text', 'Lisainfo');
    });

    cy.contains('button', 'Salvesta').click();

    cy.get('#companyName').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');
    cy.get('#companyRegistryCode').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');
    cy.get('#companyParticipantsCount').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');
    cy.get('#companyPaymentType').should('have.class', 'is-invalid').parent().contains('See väli on kohustuslik');

    cy.get('#companyName').type('Polybius Tech OU');

    cy.get('#companyRegistryCode').type('1123123123123213');
    cy.get('#companyRegistryCode').should('have.class', 'is-invalid').parent().contains('Registrikood ei ole valiidne');
    cy.get('#companyRegistryCode').type('{selectall}{backspace}14812701');

    cy.get('#companyParticipantsCount').type('1500');
    cy.get('#companyParticipantsCount').should('have.class', 'is-invalid').parent().contains('Maksimaalne osavõtjate arv on 100');
    cy.get('#companyParticipantsCount').type('{selectall}{backspace}20');

    cy.get('#companyPaymentType').select('Pangaülekanne');

    cy.get('#companyInfo').type('Mingi lisainfo');
    cy.contains('span', '14 / 5000');

    cy.contains('button', 'Salvesta').click();

    cy.get('#indexCol').should('contain.text', '1.');
    cy.get('#fullNameCol').should('contain.text', 'Polybius Tech OU');
    cy.get('#codeCol').should('contain.text', '14812701');
    cy.get('#actionCol').within(() => {
      cy.get('#participantDetailsBtn').click();
      cy.location('pathname').should('equal', '/company/1');
    });

    // Ettevõtte detailide muutmine
    cy.get('#companyName').type('{selectall}{backspace}Mingi ettevõtte OÜ');
    cy.contains('button', 'Salvesta').click();

    cy.contains('Toimunud üritused').parent().within(() => {
      cy.get('#participantsBtn').click();
    });

    cy.get('#fullNameCol').should('contain.text', 'Mingi ettevõtte OÜ');

    // Ettevõtte kustutamine
    cy.get('#removeParticipantBtn').click();
    cy.contains('Ei ole ühtegi osavõtja');
  });
});
