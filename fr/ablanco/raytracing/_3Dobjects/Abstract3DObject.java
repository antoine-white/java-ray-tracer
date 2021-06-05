package fr.ablanco.raytracing._3Dobjects;

import fr.ablanco.raytracing.Color;
import fr.ablanco.raytracing.Utils.Vec3f;

public abstract class Abstract3DObject {
    public abstract double getIntersection(Vec3f P,Vec3f v);
    public abstract Color getColor();
    public abstract float getMaterialShiness();
    public Color getSpecularColor(){
        return getColor();
    };
    public abstract float getReflexionIndex();
    public abstract float getRefractionIndex();
    public abstract Vec3f getNormal(Vec3f p);
}
