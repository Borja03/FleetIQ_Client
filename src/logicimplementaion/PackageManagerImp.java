///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package logicimplementaion;
//
//import exception.CreateException;
//import exception.DeleteException;
//import exception.SelectException;
//import exception.UpdateException;
//import java.util.Date;
//import java.util.List;
//import logicInterface.PackageManager;
//import models.Paquete;
//import models.PackageSize;
//import service.PackageRESTClient;
//
///**
// *
// * @author Omar
// */
//public class PackageManagerImp implements PackageManager {
//
//    private PackageRESTClient webClient;
//
//    public PackageManagerImp() {
//        this.webClient = new PackageRESTClient();
//    }
//
//    @Override
//    public Paquete addPackage(Paquete paquete) throws CreateException {
//        try {
//            return webClient.createPackage(paquete, Paquete.class);
//        } catch (Exception e) {
//            throw new CreateException("Error creating package", e);
//        }
//    }
//
//    @Override
//    public Paquete updatePackage(Paquete paquete) throws UpdateException {
//        try {
//            return webClient.updatePackage(paquete, Paquete.class, paquete.getId().toString());
//        } catch (Exception e) {
//            throw new UpdateException("Error updating package", e);
//        }
//    }
//
//    @Override
//    public void deletePackages(Integer idPaquete) throws DeleteException {
//        try {
//            webClient.deletePackage(idPaquete.toString());
//        } catch (Exception e) {
//            throw new DeleteException("Error deleting package", e);
//        }
//    }
//
//    @Override
//    public List<Paquete> findAllPackages() throws SelectException {
//        try {
//            return webClient.findAllPackages(); // No arguments passed
//        } catch (Exception e) {
//            throw new SelectException("Error fetching all packages", e);
//        }
//    }
//
//    @Override
//    public List<Paquete> findAllPackageBySize(PackageSize size) throws SelectException {
//        try {
//            return webClient.findPackagesBySize(List.class, size.name());
//        } catch (Exception e) {
//            throw new SelectException("Error fetching packages by size", e);
//        }
//    }
//
//    @Override
//    public List<Paquete> findAllPackagesByDates(Date firstDate, Date secondDate) throws SelectException {
//        try {
//            return webClient.findPackagesByDates(List.class, firstDate.toString(), secondDate.toString());
//        } catch (Exception e) {
//            throw new SelectException("Error fetching packages by dates", e);
//        }
//    }
//
//    @Override
//    public List<Paquete> findAllPackagesByName(String senderOrReceiverName) throws SelectException {
//        try {
//            return webClient.findPackagesByName(List.class, senderOrReceiverName);
//        } catch (Exception e) {
//            throw new SelectException("Error fetching packages by name", e);
//        }
//    }
//}
