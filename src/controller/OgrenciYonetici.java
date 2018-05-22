/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Ogrenci;
import org.hibernate.Session;

/**
 *
 * @author melihsinik
 */
public class OgrenciYonetici {
    private JTable ogrenciTablo;
    private final static String SORGU_KALIP = "from Ogrenci m";
    private Session session;
    private Vector<String> sutunlar = new Vector<>();
    private Vector<Object> satir;
    private DefaultTableModel model;
    public OgrenciYonetici(JTable ogrenciTablo) {
        this.ogrenciTablo = ogrenciTablo;
        sutunlar.add("Öğrencii ID");
        sutunlar.add("Öğrenci No");
        sutunlar.add("Ad Soyad");
        sutunlar.add("Şehir");
        sutunlar.add("Tel No");
        model=(DefaultTableModel)ogrenciTablo.getModel();
        model.setColumnIdentifiers(sutunlar);
    }

    public void musteriGetir(String aranan, String filtre) {
        String sorguMetin = "";
        if (filtre.equalsIgnoreCase("ad")) {
            sorguMetin = SORGU_KALIP + " where m.adsoyad like '%" + aranan + "%'";
        } else if (filtre.equalsIgnoreCase("adres")) {
            sorguMetin = SORGU_KALIP + " where m.adres like '%" + aranan + "%'";
        }
        else{
             sorguMetin = SORGU_KALIP;
        }
        session.beginTransaction();
        List ogrencilerList = session.createQuery(sorguMetin).list();
        session.getTransaction().commit();
        musteriGoster(ogrencilerList);

    }

    public void ac() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void kapat() {
        session.close();
    }

    private void musteriGoster(List<Ogrenci> ogrencilerList) {
        model.getDataVector().removeAllElements();
        for (Ogrenci gelenOgrenci : ogrencilerList) {
            satir=new Vector();
            satir.add(gelenOgrenci.getOgrenciid());
            satir.add(gelenOgrenci.getOgrencino());
            satir.add(gelenOgrenci.getAdsoyad());
            satir.add(gelenOgrenci.getSehir());
            satir.add(gelenOgrenci.getTelno());
            model.addRow(satir);
        }
    }
    
      public void ekle(String ogrencino, String adsoyad, String sehir, String telno){
        Ogrenci o = new Ogrenci();
        o.setOgrencino(ogrencino);
        o.setAdsoyad(adsoyad);
        o.setSehir(sehir);
        o.setTelno(telno);
        session.save(o);
    }
      public void guncelle(Integer ogrenciid, String ogrencino, String adsoyad, String sehir, String telno){
        session.close();
        session = HibernateUtil.getSessionFactory().openSession();
        Ogrenci o = new Ogrenci();
        o.setOgrenciid(ogrenciid);
        o.setOgrencino(ogrencino);
        o.setAdsoyad(adsoyad);
        o.setSehir(sehir);
        o.setTelno(telno);
        session.merge(o);
    }
      public void sil(int ogrenciid){
        session.close();
        session = HibernateUtil.getSessionFactory().openSession();
        Ogrenci b = new Ogrenci();
        b.setOgrenciid(ogrenciid);
        session.delete(b);
    }
}
