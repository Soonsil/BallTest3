precision mediump float;

uniform vec3 uLight, uLight2, uColor;

varying vec3 vNormal;
varying vec3 vPosition;

void main() {
    vec3 toLight = normalize(uLight - vPosition);
    vec3 normal = normalize(vNormal);

    // diffuse
    float diffuse = max(0.0, dot(normal, toLight));

    // blinn-phong
    float lambertian = max(dot(toLight, normal), 0.0);
    float specular = 0.0;
    vec3 specColor = vec3(1.0, 1.0, 1.0);

    if (lambertian > 0.0) {
        float shininess = 20.0;
        vec3 viewDir = normalize(-vPosition);
        vec3 halfDir = normalize(toLight + viewDir);
        float specAngle = max(dot(halfDir, normal), 0.0);

        specular = pow(specAngle, shininess);
    }

    vec3 intensity = uColor * diffuse * lambertian + specular * specColor;
    gl_FragColor = vec4(intensity, 1.0);
}
