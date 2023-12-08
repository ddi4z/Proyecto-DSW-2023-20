import { Plan } from "./plan";
import { Serie } from "../serie/serie";
import { Plataforma } from "./plataforma";

export class PlataformaDetail extends Plataforma {
    seriesPlataforma: Array<Serie>;
    planes: Array<Plan>;

    constructor(
        id: number,
        nombre: string,
        sitioWeb: string,
        logo: string,
        seriesPlataforma: Array<Serie>,
        planes: Array<Plan>
    ) {
        super(id, nombre, sitioWeb, logo);
        this.seriesPlataforma = seriesPlataforma;
        this.planes = planes;
    }
}