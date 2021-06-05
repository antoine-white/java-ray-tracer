package fr.ablanco.raytracing;

import java.util.ArrayList;
import java.util.List;

import fr.ablanco.raytracing.Utils.Pair;
import fr.ablanco.raytracing.Utils.Vec3f;
import fr.ablanco.raytracing._3Dobjects.*;

public class RayTracer {

    private static final Color DEFAULT_COLOR = new Color(0.53f, 0.81f, 0.92f, 1f);
    private static final Color AMBIANT_LIGHT_COLOR = new Color(0.13f, 0.13f, 0.13f, 1f);
    private static final Color SPECULAR_LIGHT_COLOR = new Color(.5f, .5f, .5f, 1f);
    private static final int DEFAULT_REC_DEPTH = 2;
    //to avoid floating point error; 
    //Sometimes the intersection is slightly behind so when we send back the ray to the 
    //light point for instance we hit the actual object
    private static final float DELTA = 0.001f;

    private List<Abstract3DObject> objs;
    private List<LightingPoint> lightingPoints;

    public RayTracer() {
        objs = new ArrayList<>();
        objs.add(new Sphere(new Vec3f(-22, 5, -40), 10, new Color(1f,0f,0f,1f)));
        objs.add(new Sphere(new Vec3f(0, 5, -40), 10, new Color(0f,1f,0f,1f)));
        objs.add(new Sphere(new Vec3f(22, 5, -40), 10, new Color(0f,0f,1f,1f)));
        objs.add(new Sphere(new Vec3f(-8, 0, -20), 5, new Color(1f,0f,1f,1f)));
        objs.add(new Sphere(new Vec3f(8, 0, -20), 5, new Color(0f,1f,1f,1f)));
        objs.add(new Plane(new Vec3f(0,1,0), new Vec3f(0,-5,0), new Color(1f,1f,1f,1f)));
        objs.add(new Plane(new Vec3f(0,0,-1), new Vec3f(0,0,1), new Color(1f,1f,1f,1f)));
        //objs.add(new Plane(new Vec3f(1,1,-1), new Vec3f(0,-1,0), new Color(1f,1f,1f,1f)));

        lightingPoints = new ArrayList<>();
        lightingPoints.add(new LightingPoint(new Vec3f(15f, 5f, 0f),  new Color(0.15f, 0.15f, 0.15f, 1f)));
        //lightingPoints.add(new LightingPoint(new Vec3f(-15f, 5f, 0f), new Color(0.05f, 0.05f, 0.05f, 1f)));
    }

    public Color findColor(Vec3f P, Vec3f v) {
        return findColorRec(P, v, DEFAULT_REC_DEPTH);
    }

    private Color findColorRec(Vec3f P, Vec3f v, int recArg) {

        var intersection = getIntersection(P, v);
        float lambdaI = intersection.fst();
        var minObj = intersection.snd();

        if (minObj == null) 
            return DEFAULT_COLOR;

        Vec3f I = (new Vec3f(v)).scale(lambdaI);

        Color col = Color.mult(minObj.getColor(),AMBIANT_LIGHT_COLOR);
       
        for (var lp : lightingPoints) {
            boolean visible = true;
            for (var obj : objs) {
                double f = obj.getIntersection(I, (new Vec3f(lp.point()).sub(I)));
                if (f < 1 && f >DELTA)
                    visible = false;
            }   
            if (visible) {
                col = Color.add(col,
                    Color.mult(
                        Color.mult(lp.color(), Math.max(0,(new Vec3f(minObj.getNormal(I))).dotProduct((new Vec3f(lp.point()).sub(I)).normalize()))
                        ),
                        minObj.getColor()
                ));

                Vec3f lightDir = ((new Vec3f(lp.point())).sub(I)).normalize();
                Vec3f viewDir = ((new Vec3f(P)).sub(I)).normalize();
                Vec3f halfDir = viewDir.add(lightDir).normalize();

                float specDot = Math.max(0,Vec3f.dotProduct(minObj.getNormal(I).normalize(), halfDir));
                                 
                col = Color.add(col, 
                    Color.mult(
                        (Color.mult(
                            SPECULAR_LIGHT_COLOR, 
                            (float)Math.pow((specDot), minObj.getMaterialShiness())
                        )),
                        minObj.getSpecularColor()            
                    ) 
                );
            }
        }

        if (recArg > 0) {
            // reflexion
            Vec3f n = new Vec3f(minObj.getNormal(I).normalize());
            Vec3f r = (new Vec3f(v).sub(new Vec3f(n).scale(2 * Vec3f.dotProduct(n, v))));
            Color ref = Color.mult(findColorRec(I,r,recArg-1),minObj.getReflexionIndex());
            col =  Color.add(col, ref);  
            // refraction
            if (minObj.getRefractionIndex() < 1f){
                // we assume that the reflexing indice of the second surface is 1
                double cosI = Vec3f.dotProduct(n, v);
                double sinT2 = minObj.getRefractionIndex() * minObj.getRefractionIndex() * (1.0 - cosI * cosI);
                double cosT = Math.sqrt(1.0 - sinT2);    
                Vec3f t = ((new Vec3f(v)).scale(minObj.getRefractionIndex())).add(new Vec3f(n).scale((float)(minObj.getRefractionIndex() * cosI - cosT)));                  
                Color tmp = findColorRec(I,t,recArg-1);
                Color refra = Color.mult(tmp,minObj.getRefractionIndex());
                //refraction does not work
                //col =  Color.add(col, refra);  
            }
        } 
        return col;   
    }

    private Pair<Float,Abstract3DObject> getIntersection(Vec3f P, Vec3f v){
        double minLambda = Double.MAX_VALUE;
        Abstract3DObject min = null;
        for (var obj : objs) {
            double res = obj.getIntersection(P, v);
            if (res > DELTA) {
                if (minLambda > res) {
                    minLambda = res;
                    min = obj;
                }
            }
        }
        return new Pair<>((float)minLambda,min);
    }
}
