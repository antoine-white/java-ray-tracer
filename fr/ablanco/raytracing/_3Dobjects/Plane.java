package fr.ablanco.raytracing._3Dobjects;

import fr.ablanco.raytracing.Color;
import fr.ablanco.raytracing.Utils.Vec3f;

public class Plane extends Abstract3DObject{
    
    protected final static Color DEFAULT_COLOR = new Color(1,1,1,1);

    protected Vec3f n;    
    protected float d;
    protected Color c;
    
    public Plane(Vec3f n, Vec3f point, Color c) {
        this.n = n;
        this.d = -1*(n.x*point.x + n.y*point.y + n.z*point.z);
        this.c = c;
    }

    public Plane(Vec3f n, Vec3f point) {
        this(n,point,DEFAULT_COLOR);
    }

    @Override
    public double getIntersection(Vec3f P, Vec3f v) {
        float tmp = n.dotProduct(v);
        if (tmp == 0) 
            return -1;
        else 
            return (-(P.dotProduct(n)) - d) / tmp;
    }

    @Override
    public Color getColor() {
        return c;
    }

    @Override
    public float getMaterialShiness() {
        return 80f;
    }

    @Override
    public float getReflexionIndex() {
        return 0.4f;
    }

    @Override
    public float getRefractionIndex() {
        return 0;
    }

    public Vec3f getNormal(Vec3f p){
        return n;
    }
}
