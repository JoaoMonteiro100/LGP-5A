![Dottech logo](http://i.imgur.com/TyebfdI.png "Dottech")
![BrainLight logo](http://i.imgur.com/mDt3hTP.png "BrainLight")

## CONTENTS
   
 * [Introduction](#introduction)
 * [Releases](#releases)
 * [Client](#client)
 * [Instructions](#instructions)
 * [FAQ](#faq)
 * [Maintainers](#maintainers)

 
---
 
 
## INTRODUCTION
   
Biomedical information is becoming increasingly easier to obtain thanks to wearable technology and cheaper devices. However, it’s really hard to keep up with all the software that comes with each single equipment because each of them is incompatible with all others.

We built a **framework capable of reading, analysing and displaying the information from any compatible biomedical device**. At first it’ll work with the EEG systems [Emotiv EPOC](https://emotiv.com/epoc.php) and [Neurosky Mindset](http://neurosky.com/biosensors/eeg-sensor/biosensors/), but it’s expansible so that more devices and functionalities can be easily added in the future.

The framework, **BrainLight**, is be the foundation upon which countless applications that use biosignals can be developed, but it is still an end-user solution since it also has a graphic interface showing all the information it’s receiving and analysing. To validate BrainLight’s usability we also developed a proof of concept that employs the framework’s functionalities, called **BrainStream**. It consists on a web app on which a physician can check the real-time and past signals of a patient, enabling long-distance consultations.


---
 
 
## RELEASES

Below are the release notes for each release so far:
* [Version 0.5](Releases/0-5.md)
* [Version 1.0](Releases/1-0.md)


---
 
 
## CLIENT

This project was developed for INOVA+, a company specialized in research and development in the areas of health, sustainability, smart buildings and security. To learn more about them visit [their website](http://inovamais.eu/en/).


---
 
 
## INSTRUCTIONS
   
**BrainLight** and **BrainStream** are independent projects with few similarities between then. Hence, if you want to know more about their requirements, installation guides, or other specific information please refer to their own user guides:
* [BrainLight's User Manual](BrainLight/userManual.pdf)
* [BrainStream's User Manual](BrainStream/userManual.pdf)

If you are a developer and intend to know more about how these solutions were made or how to edit them, please consult their respective developer guides:
* [BrainLight's Developer Manual](BrainLight/developerManual.pdf)
* [BrainStream's Developer Manual](BrainStream/developerManual.pdf)

Note, however, that both of them are currently only available in Portuguese. If you need English instructions, please contact [João Monteiro](https://github.com/JoaoMonteiro100), team leader and project manager.


---
 
 
## FAQ
   
**Q: Why two separate solutions?**

A: When INOVA+ pitched their idea to the group, their goal was to create a framework that could handle several different devices and display the received information in a graphic interface. Their goal would be to apply that to optogenetics, hence the name **BrainLight**. Besides that, they also wanted a proof of concept that would be built on top of that framework, but let the group come up with ideas of what that solution could be. We ended up deciding on a platform where the data would be collected from the framework and sent via web to the patient's doctor, so it would be a continuous stream of information in real time, hence the name **BrainStream**.


**Q: Why did you do this project?**

A: This project was done for the course [LGP](http://lgp.fe.up.pt/), a living lab for professional skills development from the Integrated Masters in Informatics and Computing Engineering in the [Faculty of Engineering of the University of Porto](https://sigarra.up.pt/feup/en/WEB_PAGE.INICIAL) in Porto, Portugal. The goal is that companies and students work together to build solutions that may have a lasting impact in society.


**Q: What do you hope to achieve with this project?**

A: The ultimate goal of **BrainLight** is to help in studies related with optogenetics by making it easier to incorporate several different kinds of EEG devices, while also giving trustworthy information and analysing it in real time. Even though **BrainStream** was developed just as a proof of concept, it may have lasting effects in the area by enabling easier studies with EEG devices, as well as a better tool of consultation and diagnosis by neurology specialists worldwide.


**Q: Was this done solely by informatics students?**

A: No, the students were part of a fictional "company", composed of dozens of students from different areas (informatics, multimedia and design). The informatics students are mentioned [below](#maintainers); but the final product is also the result of contributions from people of other areas from their company [Dottech](http://dottech.xyz/).


**Q: Why can't I see all commits since the beginning?**

A: We chose as a group to use separate repositories for developing the product and delivering it to the company, just because for this specific project the final product is more important than its different versions over the weeks of development. However, this repository will be updated if there are any new functioning builds.


**Q: Who can I contact if I need help?**

A: These solutions belong to INOVA+, but you can either contact the [company](#client) directly or the [students](#maintainers) that developed the project.


---
 
 
## MAINTAINERS

**Group: LGP-5A**
* André Pinheiro - https://github.com/pluralism
* David Silva - https://github.com/PsyGwendok
* João Monteiro - https://github.com/JoaoMonteiro100
* José Lima - https://github.com/zlima
* Luís Natividade - https://github.com/Vacilo
* Luís Pinto - https://github.com/siualpinto
