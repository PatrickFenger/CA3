import React from 'react';

export default class Places extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            places: []
        }
    }
    componentDidMount() {
        fetch("http://b4c478ab.ngrok.io/api/places")
            .then(res => {
                return res.json();
            })
            .then(places => {
                this.setState({ places })
                console.log("places", places)
            })
    }

    render() {
        const places = this.state.places;
        console.log("render places", places);
        return (
            <div className="container">
                <div className="row">
                    {
                        places.map((place) => {
                            return (
                                <div key={place.id} className="col-sm-2" style={{ width: 254 }}>
                                    <img  src={place.imageUrl} style={{ width: 250 }} />
                                    <div >
                                        <h4 >{place.city}</h4>
                                        <p >{place.description}</p>
                                    </div>
                                </div>
                            )

                        })
                    }
                </div>
            </div>
        )
    }
}

