/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Address;
import entity.Cityinfo;
import entity.Company;
import entity.Hobby;
import entity.Infoentity;
import entity.Person;
import entity.Phone;
import facade.exceptions.IllegalOrphanException;
import facade.exceptions.NonexistentEntityException;
import facade.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author TimmosQuadros
 */
public class Facade {

    private AddressJpaController ajpa;
    private CityinfoJpaController cijpa;
    private CompanyJpaController cojpa;
    private HobbyJpaController hjpa;
    private InfoentityJpaController ijpa;
    private PersonJpaController pejpa;
    private PhoneJpaController phjpa;

    EntityManagerFactory emf;

    public Facade(EntityManagerFactory emf) {
        ajpa = new AddressJpaController(emf);
        cijpa = new CityinfoJpaController(emf);
        cojpa = new CompanyJpaController(emf);
        hjpa = new HobbyJpaController(emf);
        ijpa = new InfoentityJpaController(emf);
        pejpa = new PersonJpaController(emf);
        phjpa = new PhoneJpaController(emf);
    }

    //Create
    public void createAddress(Address address) throws Exception {
        ajpa.create(address);
    }

    public void createCityInfo(Cityinfo cityinfo) throws Exception {
        cijpa.create(cityinfo);
    }

    public void createCompany(Company company) throws Exception {
        cojpa.create(company);
    }

    public void createHobby(Hobby hobby) throws Exception {
        hjpa.create(hobby);
    }

    public void createInfoEntity(Infoentity infoentity) throws Exception {
        ijpa.create(infoentity);
    }

    public void createPerson(Person person) throws Exception {
        pejpa.create(person);
    }

    public void createPhone(Phone phone) throws Exception {
        phjpa.create(phone);
    }

    //Retreive
    public Address getAddress(int id) {
        return ajpa.findAddress(id);
    }

    public Cityinfo getCityInfo(int id) {
        return cijpa.findCityinfo(id);
    }

    public Company getCompany(int id) {
        return cojpa.findCompany(id);
    }

    public Hobby getHobby(int id) {
        return hjpa.findHobby(id);
    }

    public Infoentity getInfoEntity(int id) {
        return ijpa.findInfoentity(id);
    }

    public Person getPerson(int id) {
        return pejpa.findPerson(id);
    }

    public Phone getPhone(int id) {
        return phjpa.findPhone(id);
    }

    //Retreive all
    public List<Address> getAddresses() {
        return ajpa.findAddressEntities();
    }

    public List<Cityinfo> getCityInfos() {
        return cijpa.findCityinfoEntities();
    }

    public List<Company> getCompanies() {
        return cojpa.findCompanyEntities();
    }

    public List<Hobby> getHobbies() {
        return hjpa.findHobbyEntities();
    }

    public List<Infoentity> getInfoEntities() {
        return ijpa.findInfoentityEntities();
    }

    public List<Person> getPersons() {
        return pejpa.findPersonEntities();
    }

    public List<Phone> getPhones() {
        return phjpa.findPhoneEntities();
    }
    
    //Update
    public void editAddress(Address address) throws Exception{
        ajpa.edit(address);
    }
    
    public void editCityInfos(Cityinfo cityinfo) throws Exception{
        cijpa.edit(cityinfo);
    }
    
    public void editCompany(Company company) throws Exception{
        cojpa.edit(company);
    }
    
    public void editHobby(Hobby hobby) throws Exception{
        hjpa.edit(hobby);
    }

    public void editInfoEntity(Infoentity infoentity) throws Exception{
        ijpa.edit(infoentity);
    }
    
    public void editPerson(Person person){
        pejpa.edit(person);
    }
    
    public void editPhone(Phone phone) throws Exception{
        phjpa.edit(phone);
    }
    
    //Delete
    public void deleteAddress(int id) throws IllegalOrphanException, NonexistentEntityException{
        ajpa.destroy(id);
    }
    
    public void deleteCityInfo(int id) throws NonexistentEntityException{
        cijpa.destroy(id);
    }
    
    public void deleteCompany(int id) throws IllegalOrphanException, NonexistentEntityException {
        cojpa.destroy(id);
    }
    
    public void deleteHobby(int id) throws NonexistentEntityException {
        hjpa.destroy(id);
    }
    
    public void deleteInfoentity(int id) throws IllegalOrphanException, NonexistentEntityException {
        ijpa.destroy(id);
    }
    
    public void deletePerson(int id) throws IllegalOrphanException, NonexistentEntityException {
        pejpa.destroy(id);
    }
    
    public void deletePhone(int id) throws NonexistentEntityException {
        phjpa.destroy(id);
    }
    

}
