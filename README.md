# CarInfoClassProject

Steps to Run the project on a School Computer:

1. Make sure the computer can run Linux and boot into that operating system

2. Determine if the computer has Android studio installed. If not continue with step 3, otherwise skip to step 8.

3. Download Android Studio (https://developer.android.com/studio/index.html)

4. Extracting the downloaded file to the desired install location

5. Open a command prompt and change in to the location ../android-studio/bin.

6. Run ./studio.sh from that directory. This will open a tool to install Android Studio. Just follow the default install steps. This will take sometime installing required tools.

7. If an error message comes up about mksdcard, follow the link in the error description. Follow the instructions under the Linux Libraries section. (most likely you will use the command line instuction under the 64-bit fedora option) Once that is done, press retry on the error message. If no error message came up, go to step 8.

8. At this point you should have the project already checked out. Once the project is checked out from github, 'Open an existing Android Studio Project'. Click on the final-project-lowe121 folder and then press okay.

9. An error message may pop up, just press ok.

10. If Android Studio was already installed, please make sure the repositories and program are completely up to date. If this is not done, the program will not run.

11. Now press the Play button on the top tool bar. Then create a new virtual device.

12. Pick which ever phone you want to run it on, press next, then download the latest api level. Press next once downloaded. And then finish.

13. Now you have a virtual device, press ok with the device selected.

14. At this point, the device will open up and run the app. If there are any errors please email, lowe121@mail.chapman.edu to help troubleshoot.


Steps to Run the project:

1. Download Android Studio (https://developer.android.com/studio/index.html)

2. Install Android studio (If installing on windows or mac, there is a pretty easy tool to open up. Follow the instructions in the Install-Linux-tar.txt file for instructions)
		Essentially, extract the files to the location of install. Change into android-studio/bin directory and run ./studio.sh to begin the install tool.

3. Follow  the install instructions
		while this is working through the installation process. Clone this repository.

4. Open an existing project and open the final-project-lowe121 directory

5. If android studio was already installed, please make sure gradle and any android repositories are up to date. Gradle will produce errors if anything is out of date.

6. Press the play button in the top bar. Pick either a plugged in android phone or install a Virtual device. any device above api 21 should work.

7. please contact lowe121@mail.chapman.edu for any questions or issues with running the application.

There are known bugs currently in the application. The main part of the application was a proof of concept. Time to make it aggregate useful data and graphs and make it a bit prettier.
