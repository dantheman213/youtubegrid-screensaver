# YouTube Grid Screen Saver

YouTube Grid is a cross-platform screensaver that will display YouTube videos in a grid. Share a collection of retro video games, music videos, movie trailers, or other themed video sets played in a randomized grid to customize your machine when it is locked!

![](https://i.imgur.com/xoOYBcg.gif)

## How It Works

YouTube Grid is a cross platform screensaver that allows you to download YouTube videos by simply providing a valid YouTube URL in the Settings/Config window. Videos are downloaded and stored in a cache so be aware of disk usage. Add a few videos and then hit Preview or wait for your screensaver to start and watch the show!

## Download

### Prerequisites

You may already have these needed framework(s) installed. I recommend downloading the installer first and if there are any errors or problems then try downloading these installers and reopen the app!

Java Runtime Environment 8 -- [Download here](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)

#### For Windows Users (additional)

This should come with Windows 10. If you aren't using Windows 10 or the screensaver still won't configure or run and you have installed the Java Runtime Environment, then go ahead and proceed to this step.

Microsoft .NET Framework 4.7.1 (Offline Installer) -- [Download here](https://www.microsoft.com/en-us/download/details.aspx?id=56116)

### Releases

Visit the `Releases` section of this repo or [click here](https://github.com/dantheman213/youtubegrid-screensaver/releases) to download the latest installer available.

NOTE: Currently the only installer available for this screensaver is available for Windows. Mac OS and Linux installers are actively being developed on.

## Build and Run

### Prerequisites

Java Development Environment 8 -- [Download here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

* Set `JAVA_HOME` in your environment variables. 

Download and manage Gradle to build the project or use an IDE like [Intellij IDEA](https://www.jetbrains.com/idea/).

### Helper Projects

#### General

You will need to include the RG3 team's `youtube-dl` binary in your build. You can find their project [here](https://github.com/rg3/youtube-dl).

#### Windows

In order to build for Windows you will need to build the helper project [GridLaunch](https://github.com/dantheman213/GridLaunch) for Windows platforms.

#### Mac OS

Coming soon! ... TBD!

#### Linux

Coming soon! ... TBD!

### Getting Started / Contributing

This project is an active work in progress. More info TBD!
