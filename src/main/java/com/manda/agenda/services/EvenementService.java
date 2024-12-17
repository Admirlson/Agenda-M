package com.manda.agenda.services;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.manda.agenda.dto.EvenementDTO;
import com.manda.agenda.mappers.EvenementMapper;
import com.manda.agenda.models.Evenement;
import com.manda.agenda.repositories.EvenementRepository;
import com.manda.agenda.utilitaires.HeaderEventHandler;

@Service
public class EvenementService {

    @Autowired
    EvenementRepository evenementRepository;

    @Autowired
    EvenementMapper evenementMapper;

    public void enregistrer(EvenementDTO evenementDTO) {
        evenementRepository.save(evenementMapper.toEvenement(evenementDTO));
    }

    public EvenementDTO modifier(EvenementDTO evenementDTO) {
        return evenementRepository.findById(evenementDTO.getId()).map(evenement -> {
            evenement.setId(evenementDTO.getId());
            evenement.setDate(evenement.getDate());
            evenement.setFormat(evenementDTO.getFormat());
            evenement.setType(evenementDTO.getType());
            evenement.setHeure(evenementDTO.getHeure());
            evenement.setInstitution(evenementDTO.getInstitution());
            evenement.setObjectif(evenementDTO.getObjectif());
            evenement.setStatut(evenementDTO.getStatut());
            evenement.setNouvelleDate(evenementDTO.getNouvelleDate());
            evenement.setSuivis(evenementDTO.getSuivis());
            evenement.setWhocreated(evenement.getWhocreated());
            evenement.setDatecreated(evenement.getDatecreated());
            evenement.setWhomodified(evenementDTO.getWhomodified());
            evenement.setDatemodified(evenementDTO.getDatemodified());
            evenement.setRemimderSent(evenementDTO.isReminderSent());
            return evenementMapper.toEvenementDTO(evenementRepository.save(evenement));
        }).orElseThrow(() -> new RuntimeException("Agent introuvable"));
    }

    public EvenementDTO getEvenement(int id) {
        Optional<Evenement> evenement = evenementRepository.findById(id);
        return evenementMapper.toEvenementDTO(new Evenement(evenement.get().getId(), evenement.get().getDate(),
                evenement.get().getFormat(), evenement.get().getType(), evenement.get().getHeure(),
                evenement.get().getInstitution(), evenement.get().getObjectif(), evenement.get().getStatut(),
                evenement.get().getNouvelleDate(), evenement.get().getSuivis(), evenement.get().getWhocreated(),
                evenement.get().getDatecreated(), evenement.get().getWhomodified(), evenement.get().getDatemodified(),
                evenement.get().isRemimderSent()));
    }

    public List<EvenementDTO> listEvenementDTOs() {
        List<Evenement> evenements = evenementRepository.findAll();
        List<EvenementDTO> evenementDTOs = new ArrayList<EvenementDTO>();
        for (Evenement evenement : evenements) {
            evenementDTOs.add(evenementMapper.toEvenementDTO(evenement));
        }
        return evenementDTOs;
    }

    @SuppressWarnings("resource")
    public byte[] listeEvenement(List<EvenementDTO> evenements) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            // initialiser le document
            PdfWriter pdfWriter = new PdfWriter(byteArrayOutputStream);
            PageSize landscape = PageSize.A4.rotate();
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(landscape);

            String image = "static/images/logo.png";
            String imageRight = "static/images/logo.png";

            // InputStream inputStream =
            // getClass().getClassLoader().getResourceAsStream("static/images/logo.png");
            // if (inputStream == null) {
            // throw new FileNotFoundException("L'image n'a pas été
            // trouvé==897977979=====");
            // }

            try (Document document = new Document(pdfDocument)) {
                document.setTopMargin(100);
                // Paragraph ENTETE1 = new Paragraph(
                // "MINISTÈRE DE L'ÉCONOMIE ET DES FINANCES(MEF)\nBureau du Ministre\nAgenda du
                // Ministre")
                // .setTextAlignment(TextAlignment.CENTER).setFontSize(16);

                Paragraph ENTETE1 = new Paragraph().add(new Text("MINISTÈRE DE L'ÉCONOMIE ET DES FINANCES(MEF)\n")
                        .setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(16))
                        .add(new Text("Bureau du Ministre\n")
                                .setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(14))
                        .add(new Text("Agenda du Ministre")
                                .setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(12))
                        .setTextAlignment(TextAlignment.CENTER);
                Paragraph ENTETE2 = new Paragraph(
                        "Listes des Évènements " + evenements.get(0).getStatut().toLowerCase() + "s")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(14);
                Paragraph ENTETE3 = new Paragraph("Agenda du Ministre").setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(12);

                float[] longueColonne = { 3, 3, 3, 3, 3, 8, 3, 3, 5 };

                pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE,
                        new HeaderEventHandler(image, imageRight, ENTETE1));

                // Creation de la table
                Table table = new Table(longueColonne).setHorizontalAlignment(HorizontalAlignment.CENTER);

                // Ajout des en-têtes
                table.addHeaderCell(new Cell().add(new Paragraph("Date")));
                table.addHeaderCell(new Cell().add(new Paragraph("Format")));
                table.addHeaderCell(new Cell().add(new Paragraph("Type")));
                table.addHeaderCell(new Cell().add(new Paragraph("Heure")));
                table.addHeaderCell(new Cell().add(new Paragraph("Institution")));
                table.addHeaderCell(new Cell().add(new Paragraph("Objectifs")));
                table.addHeaderCell(new Cell().add(new Paragraph("Statut")));
                table.addHeaderCell(new Cell().add(new Paragraph("Nouvelle date")));
                table.addHeaderCell(new Cell().add(new Paragraph("Suivis")));

                // Ajout des lignes

                for (int i = 0; i < evenements.size(); i++) {
                    String suivis = evenements.get(i).getSuivis() != null ? evenements.get(i).getSuivis() : "";
                    if (evenements.get(i).getNouvelleDate() != null) {
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getDate().toString())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getFormat())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getType())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getHeure())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getInstitution())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getObjectif())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getStatut())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getNouvelleDate().toString())));
                        table.addCell(new Cell().add(new Paragraph(suivis)));
                    } else {
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getDate().toString())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getFormat())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getType())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getHeure())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getInstitution())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getObjectif())));
                        table.addCell(new Cell().add(new Paragraph(evenements.get(i).getStatut())));
                        table.addCell(new Cell().add(new Paragraph("")));
                        table.addCell(new Cell().add(new Paragraph(suivis)));
                    }
                }

                // document.add(ENTETE1);
                document.add(ENTETE2);
                // document.add(ENTETE3);
                document.add(table);
            }

            // document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] generateCsv(List<EvenementDTO> evenements) throws UnsupportedEncodingException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PrintWriter writer = new PrintWriter(byteArrayOutputStream, true,
                    StandardCharsets.UTF_8);
            writer.println("Date,Format,Type,Heure,Institution,Objectifs,Statut,Nouvelle date,Suivis");

            for (EvenementDTO event : evenements) {
                String nouvelleDate = event.getNouvelleDate() != null ? String.valueOf(event.getNouvelleDate()) : "";

                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n", event.getDate().toString(), event.getFormat(),
                        event.getType(), event.getHeure(), event.getInstitution(), event.getObjectif(),
                        event.getStatut(), nouvelleDate, event.getSuivis());
                writer.flush();

            }

            writer.close();

        } catch (Exception ex) {

        }
        return byteArrayOutputStream.toByteArray();
    }

    // public void sendReminders(List<EvenementDTO> evenements) {
    // LocalDateTime tomorrowStart =
    // LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);
    // LocalDateTime tomorrowEnd = tomorrowStart.plusDays(1).minusSeconds(0);

    // }

    @SuppressWarnings("null")
    public List<EvenementDTO> listEvenementDTOs(List<EvenementDTO> listEvenementDTOs) {
        LocalDate tomorrowStart = LocalDate.now().plusDays(1);
        // LocalDateTime tomorrowEnd = tomorrowStart.plusDays(1).minusSeconds(0);
        List<EvenementDTO> list = null;
        try {
            list = new ArrayList<EvenementDTO>();
            for (int i = 0; i < listEvenementDTOs.size(); i++) {
                list.add(new EvenementDTO(listEvenementDTOs.get(i).getId(), listEvenementDTOs.get(i).getDate(),
                        listEvenementDTOs.get(i).getFormat(), listEvenementDTOs.get(i).getType(),
                        listEvenementDTOs.get(i).getHeure(), listEvenementDTOs.get(i).getInstitution(),
                        listEvenementDTOs.get(i).getObjectif(), listEvenementDTOs.get(i).getStatut(),
                        listEvenementDTOs.get(i).getNouvelleDate(), listEvenementDTOs.get(i).getSuivis(),
                        listEvenementDTOs.get(i).getWhocreated(), listEvenementDTOs.get(i).getDatecreated(),
                        listEvenementDTOs.get(i).getWhomodified(), listEvenementDTOs.get(i).getDatemodified(),
                        listEvenementDTOs.get(i).isReminderSent()));
            }

        } catch (Exception ex) {
        }
        return list.stream().filter(evenement -> {
            LocalDate dob = evenement.getDate();
            return dob != null && dob.getDayOfMonth() == tomorrowStart.getDayOfMonth();
        }).collect(Collectors.toList());
    }

    // private List<EvenementDTO>
    // findByDateTimeBetweenAndReminderSentFalse(LocalDateTime tomorrowStart,
    // LocalDateTime tomorrowEnd) {
    // List<EvenementDTO> list

    // System.out.println("tomorrowStart=======:" + tomorrowStart);
    // System.out.println("tomorrowEnd=======:" + tomorrowEnd);
    // }

    public void sendReminders(List<EvenementDTO> listEvenementDTOs) {
        for (EvenementDTO evenementDTO : listEvenementDTOs(listEvenementDTOs)) {
            System.out.println("Date======================" + evenementDTO.getDate());
            System.out.println("Heure======================" + evenementDTO.getHeure());
            sendNotification(evenementDTO);

        }
    }

    private void sendNotification(EvenementDTO evenementDTO) {
        System.out.println("Date sendNotification======================" + evenementDTO.getDate());
        senderMail("eugeneadelain@yahoo.fr", "Rappel: Évenement prévu demain",
                "L'évènement \"" + evenementDTO.getFormat() + "\"à l'" + evenementDTO.getType());
    }

    @Autowired
    private JavaMailSender mailSender;

    private void senderMail(String to, String sujet, String text) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);

            message.setSubject(sujet);

            message.setText(text);

            // message.setFrom("eugeneadelain@yahoo.fr");
            message.setFrom("eadelain@gmail.com");

            mailSender.send(message);

            System.out.println("senderMail après 3======================");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public List<EvenementDTO> listEvenementDTOParStatut(String statut) {
        List<Evenement> evenements = evenementRepository.findByStatut(statut);
        List<EvenementDTO> evenementDTOs = new ArrayList<EvenementDTO>();
        for (Evenement evenement : evenements) {
            evenementDTOs.add(evenementMapper.toEvenementDTO(evenement));
        }
        return evenementDTOs;
    }

    /**
     * @return JavaMailSender return the mailSender
     */
    public JavaMailSender getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender the mailSender to set
     */
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

}
