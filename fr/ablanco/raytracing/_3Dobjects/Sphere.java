package fr.ablanco.raytracing._3Dobjects;

import fr.ablanco.raytracing.Color;
import fr.ablanco.raytracing.Utils.Vec3f;

public class Sphere extends Abstract3DObject {
    private Vec3f position;
    private float radius;
    private Color color;

    private static final Color DEFAULT_COLOR = new Color(0f,1f,0f,1f);

    public Sphere(Vec3f position, float radius,Color c) {
        this.position = position;
        this.radius = radius;
        this.color = c;
    }
    
    public Sphere(Vec3f position, float radius) {
        this(position, radius, DEFAULT_COLOR);
    }

    @Override
    public double getIntersection(Vec3f P, Vec3f v) {
        Vec3f oc = new Vec3f();
        oc.setSub(P, position);
        float a = Vec3f.dotProduct(v,v);
        float b = 2.0f * Vec3f.dotProduct(oc,v);
        float c = Vec3f.dotProduct(oc,oc) - (radius*radius);
        float discriminant = b*b - 4*a*c;
        if(discriminant < 0)
            return -1.0;
        else{
            double s1 = (-b - Math.sqrt(discriminant)) / (2.0*a);
            double s2 = (-b + Math.sqrt(discriminant)) / (2.0*a);
            if(s1 < 0 && s2 < 0)
                return -1;
            else if(s1 < 0 && s2 > 0 )
                return s2;
            else 
                return s1;
        }
    } 

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public float getMaterialShiness() {
        return 20f;
    }

    @Override
    public float getReflexionIndex() {
        return .4f;
    }

    @Override
    public float getRefractionIndex() {
        return 0.5f;
    }

    @Override
    public Vec3f getNormal(Vec3f p){
        return ((new Vec3f(p)).sub(position));
    }

}
