# Welcome to spatialite-geometry !

This java library to convert spatial databases geometries from text to binaries and vice-versa.

Supported databases:
- Spatialite
- PostGis

Supported types:
- Well Known Text 
- Spatialite Query
- Well Known Binary
- GeoJson 

Supported Geometries:
- Point
- LineString
- Polygon
- PolyhedralSurface
- MultiLineString
- MultiPolygon

Supported Coordinates:
- XY
- XYZ
- XYM
- XYZM

# Usage

Use [JitPack](https://jitpack.io/) for Maven / Gradle package managers

## Gradle
**Step 1.** Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```css
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
```

**Step 2.** Add the dependency

```css
	dependencies {
		implementation 'com.github.YannCaron:SpatialDB-geometry:<TAG_NAME>'
	}
```
> **note:** Replace <TAG_NAME> by the project version e.g. "1.0.1"
> **see**: JitPack [gradle](https://jitpack.io/#gradle) documentation

## Maven
**Step 1.** Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```markup
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

**Step 2.** Add the dependency

```markup
	<dependency>
	    <groupId>com.github.YannCaron</groupId>
	    <artifactId>SpatialDB-geometry</artifactId>
	    <version>TAG_NAME</version>
	</dependency>
```

> **note:** Replace "TAG_NAME" by the project version e.g. "1.0.1"
> **see**: JitPack [maven](https://jitpack.io/#maven) documentation

