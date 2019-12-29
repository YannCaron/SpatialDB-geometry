# Welcome to spatialite-geometry !

This java library to convert spatial databases geometries from text to binaries and vice-versa.

Supported databases:
- Spatialite
- PostGIS

Supported types:
- Well Known Text 
- Spatialite Query
- Well Known Binary
- GeoJSON

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

# Package

Use [JitPack](https://jitpack.io/) for Maven / Gradle package managers

## Gradle
**Step 1.** Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```bash
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
```

**Step 2.** Add the dependency

```bash
	dependencies {
		implementation 'com.github.YannCaron:SpatialDB-geometry:<TAG_NAME>'
	}
```
> **note:** Replace <TAG_NAME> by the project version e.g. "1.0.2"

> **see**: JitPack [gradle](https://jitpack.io/#gradle) documentation

## Maven
**Step 1.** Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

**Step 2.** Add the dependency

```xml
	<dependency>
	    <groupId>com.github.YannCaron</groupId>
	    <artifactId>SpatialDB-geometry</artifactId>
	    <version>TAG_NAME</version>
	</dependency>
```

> **note:** Replace "TAG_NAME" by the project version e.g. "1.0.2"

> **see**: JitPack [maven](https://jitpack.io/#maven) documentation


# Usage

## Object to string

Create the object and serialize it to expected format:
- WKT
- GeoJSON
- Spatial query

``` java
        Point<XY> p = new Point<>(new XY(6.1466014, 46.2017559));
        
        System.out.println(p.toString());
        // POINT (6.45467 15.4578)
        
        System.out.println(p.toGeoJson());
        // {"type": "Point", "coordinates": [6.45467, 15.4578]}
        
        System.out.println(p.toQuery(4326));
        // ST_GeomFromText('POINT (6.45467 15.4578)', 4326)
```

## WKT to object

Unserialize from WKT

``` java
        Point<XY> p = Point.unMarshall(XY.class, "POINT (6.45467 15.4578)");

        System.out.println(p.toString());
        // POINT (6.45467 15.4578)
```

## WKB to object

Unserialize from WKB
``` java
        // Create binary point
        byte[] BYTES = new byte[]{ // POINT (10.0, 10.0)
            0x01, // 01 - Little-endian
            0x01, 0x00, 0x00, 0x00, // POINT
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x24, 0x40, // 10.0
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x24, 0x40 // 10.0
        };

        Point<XY> p = (Point<XY>) Point.unMarshall(BYTES);
        
        System.out.println(p.toString());
        // POINT (10 10)
```

