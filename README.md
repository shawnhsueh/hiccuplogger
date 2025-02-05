# 📱 Hiccup & Meal Logger

## 📖 Overview
Hiccup & Meal Logger is a simple **Android app** that allows users to **log hiccups and meals** with timestamps. Users can:

- **Record Hiccups** (`h YYYY-MM-DD HH:MM:SS`)
- **Record Meals** (`m YYYY-MM-DD HH:MM:SS`)
- **View, Import, Export, and Delete Logs**
- **Use a Widget for Quick Hiccup Logging**

---
##  Features & Usage
*  **Record a Hiccup**: Tap **"Record Hiccup"** button in the app or use the widget.
*  **Home Screen Widget**: Tap the widget to log a hiccup instantly.
*  **Record a Meal**: Tap **"Record Meal"** to log a meal.
*  **Timestamped Entries**: Hiccups and meals are logged with date & time.
*  **Import Logs**: Manually enter text and append it to the log.
  - Type in the input box & tap **"Import Text"**.
*  **Export Logs**: Save logs as `.txt` in the Downloads folder.
  - Tap **"Export Log"** to save the `.txt` file to Downloads.
*  **Delete Last Entry**: Remove the most recent log entry.
  - Tap **"Delete Last Line"** to remove the latest log entry.
*  **Scrollable Log View**: View full logs inside the app.
  - Tap **"Show Log"** to display the recorded entries.


---

## 📷 App UI
<img src="https://github.com/shawnhsueh/hiccuplogger/blob/main/screenshot/appUI1.png?raw=true" width="500"  />


---

## Example log file
A example log file would look like the following.
```
h 2025-02-05 17:58:26
h 2025-02-05 18:10:54
m 2025-02-05 18:20:33
h 2025-02-05 18:25:19
h 2025-02-05 18:26:07
h 2025-02-05 18:30:06
h 2025-02-05 18:37:18
```

---

##  Installation
### Clone the Repository
```sh
git clone https://github.com/shawnhsueh/hiccuplogger.git
cd hiccuplogger
```
### Open in Android Studio
- File → Open → Select the project folder.
### Run the App
```sh
Shift + F10  # Or click ▶ button in Android Studio
```
### Deploy to a Physical Device
- Enable Developer Mode & USB Debugging.
- Connect your Cellphone
- Select the device & click **Run**.
